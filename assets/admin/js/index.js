function handleFile(files) {
  const file = files[0]; // Récupère le premier fichier sélectionné
  if (!file) return; // Si aucun fichier n'est sélectionné, quitte la fonction

  const reader = new FileReader(); // Crée un objet FileReader pour lire le contenu du fichier

  // Fonction appelée lorsque la lecture du fichier est terminée
  reader.onload = function (event) {
    const fileContent = event.target.result; // Récupère le contenu du fichier
    parseAndDisplayCSV(fileContent); // Parse le contenu CSV et l'affiche dans le tableau
    // Affiche les boutons Envoyer et Supprimer
    document.getElementById("sendColumnsButton").style.display = "inline-block";
    document.getElementById("deleteButton").style.display = "inline-block";
    // Cache le bouton Télécharger
    document.getElementById("fileInput").style.display = "none";
    document.getElementById("downloadButton").style.display = "inline-block";
    // Cache le bouton Connexion BD
    document.getElementById("connexionButton").style.display = "none";

    const buttonMandat = document.getElementById("ManualInputMandat");
    const buttonCredit = document.getElementById("ManualInputCredit");
    const buttonRecette = document.getElementById("ManualInputRecettes");
    const buttonDettes = document.getElementById("ManualInputDettes");

    if (buttonMandat) {
      // Cache le bouton Saisie Manuelle Mandat
      //alert("buttonMandat Trouvé");
      buttonMandat.style.display = "none";
    } else if (buttonCredit) {
      // cache le bouton Saisie Manuelle Crédit
      //alert("buttonCredit Trouvé");
      buttonCredit.style.display = "none";
    } else if (buttonRecette) {
      // cache le bouton Saisie Manuelle Recette
      //alert("buttonRecette Trouvé");
      buttonRecette.style.display = "none";
    } else if (buttonDettes) {
      // cache le bouton Saisie Manuelle Dettes
      //alert("buttonDettes Trouvé");
      buttonDettes.style.display = "none";
    }
  };

  reader.readAsText(file); // Lit le contenu du fichier en tant que texte
}

// Fonction pour parser le contenu CSV et afficher le tableau avec des cases à cocher pour chaque colonne
function parseAndDisplayCSV(content) {
  const lines = content.trim().split("\n"); // Sépare le contenu en lignes
  const headers = lines[0].split("|"); // Les en-têtes sont dans la première ligne
  const data = lines.slice(1); // Les données commencent à partir de la deuxième ligne

  // Crée le tableau HTML
  const table = document.createElement("table");
  table.classList.add("csv-table");

  // Ajoute l'en-tête du tableau avec des cases à cocher
  // Ajoute l'en-tête du tableau avec des cases à cocher
  const headerRow = document.createElement("tr");
  headers.forEach((headerText, index) => {
    // Ajoutez également l'index de colonne ici
    const th = document.createElement("th");
    const checkbox = document.createElement("input");
    checkbox.type = "checkbox";
    checkbox.value = headerText;
    checkbox.dataset.columnIndex = index; // Définit l'attribut data-column-index
    checkbox.checked = true; // Toutes les cases à cocher sont cochées par défaut (ligne ajoutée)
    checkbox.addEventListener("change", () => updateSelectedColumns(checkbox));
    th.appendChild(checkbox);
    th.appendChild(document.createTextNode(headerText));
    headerRow.appendChild(th);
  });
  table.appendChild(headerRow);

  // Ajoute les lignes de données
  data.forEach((line) => {
    const rowData = line.split("|");
    const row = document.createElement("tr");
    rowData.forEach((cellData) => {
      const cell = document.createElement("td");
      cell.textContent = cellData;
      row.appendChild(cell);
    });
    table.appendChild(row);
  });

  // Affiche le tableau dans l'élément 'fileContent'
  const fileContentElement = document.getElementById("fileContent");
  fileContentElement.innerHTML = ""; // Vide le contenu précédent
  fileContentElement.appendChild(table);
}

// Fonction pour mettre à jour les colonnes sélectionnées
function updateSelectedColumns(checkbox) {
  const selectedColumns = document.getElementById("selectedColumns");
  if (checkbox.checked) {
    // Ajoute le nom de la colonne à la liste des colonnes sélectionnées
    selectedColumns.textContent += checkbox.value + ", ";
  } else {
    // Supprime le nom de la colonne de la liste des colonnes sélectionnées
    const regex = new RegExp(checkbox.value + ", ", "g");
    selectedColumns.textContent = selectedColumns.textContent.replace(
      regex,
      ""
    );
  }
}

function sendSelectedColumnsDepenses() {
  const selectedColumns = document.querySelectorAll(
    'input[type="checkbox"]:checked'
  );
  // Créer un objet pour stocker les colonnes et leurs valeurs
  const columnsData = {};

  // Parcourir chaque colonne sélectionnée
  // Parcourir chaque ligne de la table pour extraire la valeur de cette colonne
  selectedColumns.forEach((checkbox) => {
    // Récupérer l'index de colonne à partir de l'attribut data
    const columnIndex = checkbox.dataset.columnIndex;

    // Parcourir chaque ligne de la table pour extraire la valeur de cette colonne
    const tableRows = document.querySelectorAll(".csv-table tr");
    tableRows.forEach((row) => {
      // Récupérer la cellule correspondant à l'index de colonne
      const cell = row.querySelectorAll("td")[columnIndex];
      const columnName = checkbox.value;

      // Vérifier si la cellule correspond à la colonne sélectionnée et ajouter sa valeur à l'objet columnsData
      if (cell) {
        if (!columnsData[columnName]) {
          columnsData[columnName] = []; // Initialiser un tableau pour stocker les valeurs de la colonne si nécessaire
        }
        columnsData[columnName].push(cell.textContent);
      }
    });
  });

  // Copie de l'objet original pour ne pas modifier les données d'origine
  const modifiedData = {};

  // Parcourir chaque propriété de l'objet columnsData
  for (const key in columnsData) {
    if (Object.hasOwnProperty.call(columnsData, key)) {
      const value = columnsData[key];
      const modifiedKey = key.replace(/\r/g, ""); // Supprimer les sauts de ligne de la clé

      // Modifier les valeurs du tableau en supprimant les sauts de ligne
      const modifiedValues = value.map((item) => item.replace(/\r/g, ""));

      // Ajouter la paire clé-valeurs modifiée à l'objet copié
      modifiedData[modifiedKey] = modifiedValues;
    }
  }

  // Exemple de données à envoyer au serveur
  const data = {
    colonnes: modifiedData, // Utilisez la bonne clé
  };

  // Exemple de requête AJAX
  fetch("/depenses", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  })
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      throw new Error("Erreur lors de la requête.");
    })
    .then((data) => {
      // Traiter la réponse du serveur
      alert(data.message);
    })
    .catch((error) => {
      // Gérer les erreurs
      console.error("Erreur:", error);
    });

  // cache les boutons Envoyer et Supprimer
  document.getElementById("sendColumnsButton").style.display = "none";
  document.getElementById("deleteButton").style.display = "none";
  // affiche le bouton Télécharger
  //document.getElementById("fileInput").style.display = "inline-block";
  document.getElementById("downloadButton").style.display = "inline-block";
  // affiche le bouton Connexion BD
  document.getElementById("connexionButton").style.display = "inline-block";

  const buttonMandat = document.getElementById("ManualInputMandat");
  const buttonCredit = document.getElementById("ManualInputCredit");
  const buttonRecette = document.getElementById("ManualInputRecettes");
  const buttonDettes = document.getElementById("ManualInputDettes");

  if (buttonMandat) {
    // affiche le bouton Saisie Manuelle Mandat
    //alert("buttonMandat Trouvé");
    buttonMandat.style.display = "inline-block";
  } else if (buttonCredit) {
    // affiche le bouton Saisie Manuelle Crédit
    //alert("buttonCredit Trouvé");
    buttonCredit.style.display = "inline-block";
  } else if (buttonRecette) {
    // affiche le bouton Saisie Manuelle Recette
    //alert("buttonRecette Trouvé");
    buttonRecette.style.display = "inline-block";
  } else if (buttonDettes) {
    // affiche le bouton Saisie Manuelle Dettes
    //alert("buttonDettes Trouvé");
    buttonDettes.style.display = "inline-block";
  }
}




function sendSelectedColumnsCredit() {
  const selectedColumns = document.querySelectorAll(
    'input[type="checkbox"]:checked'
  );
  // Créer un objet pour stocker les colonnes et leurs valeurs
  const columnsData = {};

  // Parcourir chaque colonne sélectionnée
  // Parcourir chaque ligne de la table pour extraire la valeur de cette colonne
  selectedColumns.forEach((checkbox) => {
    // Récupérer l'index de colonne à partir de l'attribut data
    const columnIndex = checkbox.dataset.columnIndex;

    // Parcourir chaque ligne de la table pour extraire la valeur de cette colonne
    const tableRows = document.querySelectorAll(".csv-table tr");
    tableRows.forEach((row) => {
      // Récupérer la cellule correspondant à l'index de colonne
      const cell = row.querySelectorAll("td")[columnIndex];
      const columnName = checkbox.value;

      // Vérifier si la cellule correspond à la colonne sélectionnée et ajouter sa valeur à l'objet columnsData
      if (cell) {
        if (!columnsData[columnName]) {
          columnsData[columnName] = []; // Initialiser un tableau pour stocker les valeurs de la colonne si nécessaire
        }
        columnsData[columnName].push(cell.textContent);
      }
    });
  });

  // Copie de l'objet original pour ne pas modifier les données d'origine
  const modifiedData = {};

  // Parcourir chaque propriété de l'objet columnsData
  for (const key in columnsData) {
    if (Object.hasOwnProperty.call(columnsData, key)) {
      const value = columnsData[key];
      const modifiedKey = key.replace(/\r/g, ""); // Supprimer les sauts de ligne de la clé

      // Modifier les valeurs du tableau en supprimant les sauts de ligne
      const modifiedValues = value.map((item) => item.replace(/\r/g, ""));

      // Ajouter la paire clé-valeurs modifiée à l'objet copié
      modifiedData[modifiedKey] = modifiedValues;
    }
  }

  // Exemple de données à envoyer au serveur
  const data = {
    colonnes: modifiedData, // Utilisez la bonne clé
  };

  // Exemple de requête AJAX
  fetch("/Credits", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  })
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      throw new Error("Erreur lors de la requête.");
    })
    .then((data) => {
      // Traiter la réponse du serveur
      alert(data.message);
    })
    .catch((error) => {
      // Gérer les erreurs
      console.error("Erreur:", error);
    });

  // cache les boutons Envoyer et Supprimer
  document.getElementById("sendColumnsButton").style.display = "none";
  document.getElementById("deleteButton").style.display = "none";
  // affiche le bouton Télécharger
  //document.getElementById("fileInput").style.display = "inline-block";
  document.getElementById("downloadButton").style.display = "inline-block";
  // affiche le bouton Connexion BD
  document.getElementById("connexionButton").style.display = "inline-block";

  const buttonMandat = document.getElementById("ManualInputMandat");
  const buttonCredit = document.getElementById("ManualInputCredit");
  const buttonRecette = document.getElementById("ManualInputRecettes");
  const buttonDettes = document.getElementById("ManualInputDettes");

  if (buttonMandat) {
    // affiche le bouton Saisie Manuelle Mandat
    //alert("buttonMandat Trouvé");
    buttonMandat.style.display = "inline-block";
  } else if (buttonCredit) {
    // affiche le bouton Saisie Manuelle Crédit
    //alert("buttonCredit Trouvé");
    buttonCredit.style.display = "inline-block";
  } else if (buttonRecette) {
    // affiche le bouton Saisie Manuelle Recette
    //alert("buttonRecette Trouvé");
    buttonRecette.style.display = "inline-block";
  } else if (buttonDettes) {
    // affiche le bouton Saisie Manuelle Dettes
    //alert("buttonDettes Trouvé");
    buttonDettes.style.display = "inline-block";
  }
}




function sendSelectedColumnsRecettes() {
  const selectedColumns = document.querySelectorAll(
    'input[type="checkbox"]:checked'
  );
  // Créer un objet pour stocker les colonnes et leurs valeurs
  const columnsData = {};

  // Parcourir chaque colonne sélectionnée
  // Parcourir chaque ligne de la table pour extraire la valeur de cette colonne
  selectedColumns.forEach((checkbox) => {
    // Récupérer l'index de colonne à partir de l'attribut data
    const columnIndex = checkbox.dataset.columnIndex;

    // Parcourir chaque ligne de la table pour extraire la valeur de cette colonne
    const tableRows = document.querySelectorAll(".csv-table tr");
    tableRows.forEach((row) => {
      // Récupérer la cellule correspondant à l'index de colonne
      const cell = row.querySelectorAll("td")[columnIndex];
      const columnName = checkbox.value;

      // Vérifier si la cellule correspond à la colonne sélectionnée et ajouter sa valeur à l'objet columnsData
      if (cell) {
        if (!columnsData[columnName]) {
          columnsData[columnName] = []; // Initialiser un tableau pour stocker les valeurs de la colonne si nécessaire
        }
        columnsData[columnName].push(cell.textContent);
      }
    });
  });

  // Copie de l'objet original pour ne pas modifier les données d'origine
  const modifiedData = {};

  // Parcourir chaque propriété de l'objet columnsData
  for (const key in columnsData) {
    if (Object.hasOwnProperty.call(columnsData, key)) {
      const value = columnsData[key];
      const modifiedKey = key.replace(/\r/g, ""); // Supprimer les sauts de ligne de la clé

      // Modifier les valeurs du tableau en supprimant les sauts de ligne
      const modifiedValues = value.map((item) => item.replace(/\r/g, ""));

      // Ajouter la paire clé-valeurs modifiée à l'objet copié
      modifiedData[modifiedKey] = modifiedValues;
    }
  }

  // Exemple de données à envoyer au serveur
  const data = {
    colonnes: modifiedData, // Utilisez la bonne clé
  };

  // Exemple de requête AJAX
  fetch("/Recettes", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  })
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      throw new Error("Erreur lors de la requête.");
    })
    .then((data) => {
      // Traiter la réponse du serveur
      alert(data.message);
    })
    .catch((error) => {
      // Gérer les erreurs
      console.error("Erreur:", error);
    });

  // cache les boutons Envoyer et Supprimer
  document.getElementById("sendColumnsButton").style.display = "none";
  document.getElementById("deleteButton").style.display = "none";
  // affiche le bouton Télécharger
  //document.getElementById("fileInput").style.display = "inline-block";
  document.getElementById("downloadButton").style.display = "inline-block";
  // affiche le bouton Connexion BD
  document.getElementById("connexionButton").style.display = "inline-block";

  const buttonMandat = document.getElementById("ManualInputMandat");
  const buttonCredit = document.getElementById("ManualInputCredit");
  const buttonRecette = document.getElementById("ManualInputRecettes");
  const buttonDettes = document.getElementById("ManualInputDettes");

  if (buttonMandat) {
    // affiche le bouton Saisie Manuelle Mandat
    //alert("buttonMandat Trouvé");
    buttonMandat.style.display = "inline-block";
  } else if (buttonCredit) {
    // affiche le bouton Saisie Manuelle Crédit
    //alert("buttonCredit Trouvé");
    buttonCredit.style.display = "inline-block";
  } else if (buttonRecette) {
    // affiche le bouton Saisie Manuelle Recette
    //alert("buttonRecette Trouvé");
    buttonRecette.style.display = "inline-block";
  } else if (buttonDettes) {
    // affiche le bouton Saisie Manuelle Dettes
    //alert("buttonDettes Trouvé");
    buttonDettes.style.display = "inline-block";
  }
}




function sendSelectedColumnsDettes() {
  const selectedColumns = document.querySelectorAll(
    'input[type="checkbox"]:checked'
  );
  // Créer un objet pour stocker les colonnes et leurs valeurs
  const columnsData = {};

  // Parcourir chaque colonne sélectionnée
  // Parcourir chaque ligne de la table pour extraire la valeur de cette colonne
  selectedColumns.forEach((checkbox) => {
    // Récupérer l'index de colonne à partir de l'attribut data
    const columnIndex = checkbox.dataset.columnIndex;

    // Parcourir chaque ligne de la table pour extraire la valeur de cette colonne
    const tableRows = document.querySelectorAll(".csv-table tr");
    tableRows.forEach((row) => {
      // Récupérer la cellule correspondant à l'index de colonne
      const cell = row.querySelectorAll("td")[columnIndex];
      const columnName = checkbox.value;

      // Vérifier si la cellule correspond à la colonne sélectionnée et ajouter sa valeur à l'objet columnsData
      if (cell) {
        if (!columnsData[columnName]) {
          columnsData[columnName] = []; // Initialiser un tableau pour stocker les valeurs de la colonne si nécessaire
        }
        columnsData[columnName].push(cell.textContent);
      }
    });
  });

  // Copie de l'objet original pour ne pas modifier les données d'origine
  const modifiedData = {};

  // Parcourir chaque propriété de l'objet columnsData
  for (const key in columnsData) {
    if (Object.hasOwnProperty.call(columnsData, key)) {
      const value = columnsData[key];
      const modifiedKey = key.replace(/\r/g, ""); // Supprimer les sauts de ligne de la clé

      // Modifier les valeurs du tableau en supprimant les sauts de ligne
      const modifiedValues = value.map((item) => item.replace(/\r/g, ""));

      // Ajouter la paire clé-valeurs modifiée à l'objet copié
      modifiedData[modifiedKey] = modifiedValues;
    }
  }

  // Exemple de données à envoyer au serveur
  const data = {
    colonnes: modifiedData, // Utilisez la bonne clé
  };

  // Exemple de requête AJAX
  fetch("/Dettes", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  })
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      throw new Error("Erreur lors de la requête.");
    })
    .then((data) => {
      // Traiter la réponse du serveur
      alert(data.message);
    })
    .catch((error) => {
      // Gérer les erreurs
      console.error("Erreur:", error);
    });

  // cache les boutons Envoyer et Supprimer
  document.getElementById("sendColumnsButton").style.display = "none";
  document.getElementById("deleteButton").style.display = "none";
  // affiche le bouton Télécharger
  //document.getElementById("fileInput").style.display = "inline-block";
  document.getElementById("downloadButton").style.display = "inline-block";
  // affiche le bouton Connexion BD
  document.getElementById("connexionButton").style.display = "inline-block";

  const buttonMandat = document.getElementById("ManualInputMandat");
  const buttonCredit = document.getElementById("ManualInputCredit");
  const buttonRecette = document.getElementById("ManualInputRecettes");
  const buttonDettes = document.getElementById("ManualInputDettes");

  if (buttonMandat) {
    // affiche le bouton Saisie Manuelle Mandat
    //alert("buttonMandat Trouvé");
    buttonMandat.style.display = "inline-block";
  } else if (buttonCredit) {
    // affiche le bouton Saisie Manuelle Crédit
    //alert("buttonCredit Trouvé");
    buttonCredit.style.display = "inline-block";
  } else if (buttonRecette) {
    // affiche le bouton Saisie Manuelle Recette
    //alert("buttonRecette Trouvé");
    buttonRecette.style.display = "inline-block";
  } else if (buttonDettes) {
    // affiche le bouton Saisie Manuelle Dettes
    //alert("buttonDettes Trouvé");
    buttonDettes.style.display = "inline-block";
  }
}


const buttonMandat = document.getElementById("ManualInputMandat");
const buttonCredit = document.getElementById("ManualInputCredit");
const buttonRecette = document.getElementById("ManualInputRecettes");
const buttonDettes = document.getElementById("ManualInputDettes");

if (buttonCredit) {
  // Cache le bouton Saisie Manuelle Mandat
  //alert("buttonCredit Trouvé");
  document
    .getElementById("ManualInputCredit")
    .addEventListener("click", function () {
      document.getElementById("fileContent").innerHTML = `
      <div class="bg">
      <div class="container h-100">
        <div class="row h-100 justify-content-center align-items-center">
          <div class="col-8">
            <form id="CreditsForm" method="POST" action="/Credits" class="form-container">
                <h2 class="text-center">Saisie crédit</h2>

                <div class="form-group">
                    <label for="textInput1">Gestion</label>
                    <input type="text" class="form-control" id="GESTION" name="GESTION" placeholder="2023" required="required">
                </div>



                <div class="form-group">
                    <label for="textInput2">Mois</label>
                    <input type="text" class="form-control" id="MOIS" name="MOIS" placeholder="12" required="required">
                </div>



                <div class="form-group">
                    <label for="textInput3">Ordonnateur</label>
                    <input type="text" class="form-control" id="ORD" name="ORD" placeholder="108044" required="required">
                </div>



                <div class="form-group">
                    <label for="textInput4">Code Portfeuille</label>
                    <input type="text" class="form-control" id="PORTEF" name="PORTEF" placeholder="011" required="required">
                </div>



                <div class="form-group">
                    <label for="textInput5">Programme</label>
                    <input type="text" class="form-control" id="PROG" name="PROG" placeholder="044" required="required">
                </div>




                <div class="form-group">
                    <label for="textInput6">Sous Programme</label>
                    <input type="text" class="form-control" id="S_PROG" name="S_PROG" placeholder="01" required="required">
                </div>




                <div class="form-group">
                   <label for="textInput7">Action</label>
                   <input type="text" class="form-control" id="ACTION" name="ACTION" placeholder="2046" required="required">
                </div>




                <div class="form-group">
                    <label for="textInput8">Sous Action</label>
                    <input type="text" class="form-control" id="S_ACTION" name="S_ACTION" placeholder="000" required="required">
                </div>





                <div class="form-group">
                    <label for="textInput10">Axe économique</label>
                    <input type="text" class="form-control" id="AXE_ECO" name="AXE_ECO" placeholder="25310" required="required">
                </div>




                <div class="form-group">
                    <label for="textInput11">Disponibilité</label>
                    <input type="text" class="form-control" id="DISPOS" name="DISPOS" placeholder="0" required="required">
                </div>




                <div class="form-group">
                    <label for="textInput12">Montant Crédit</label>
                    <input type="text" class="form-control" id="TOT_CREDIT" name="TOT_CREDIT" placeholder="2664000" required="required">
                </div>




                <div class="form-group">
                    <label for="textInput13">Montant Consomé</label>
                    <input type="text" class="form-control" id="TOT_DEB" name="TOT_DEB" placeholder="2663421" required="required">
                </div>


                <div class="form-group">
                    <label for="textInput13">Solde</label>
                    <input type="text" class="form-control" id="SOLDE" name="SOLDE" placeholder="579" required="required">
                </div>



                <div style="display: flex; justify-content: center; margin-left: auto; margin-right: auto;">
                    <button type="submit" class="btn btn-primary" style="margin-right: 20px;">Envoyer</button>
                    <button type="reset" class="btn btn-secondary">Annuler</button>
                </div>



           </form>
         </div>
       </div>
     </div>
   </div>
  `;

      // Ajouter des styles pour le formulaire
      const style = document.createElement("style");
      style.innerHTML = `
    .bg {
      background-color: #0000;
      padding: 0px;
      border-radius: 10px;
      margin-top: -220px; /* Ajuster la marge supérieure */
    }
    .form-container {
      background-color: #062a4e; 
      border-color: #062a4e;
      color: white;
      padding: 20px;
      border-radius: 10px;
      box-shadow: 0px 10px 10px 10px rgba(173, 216, 230, 0.5);
    }

    /* Styles pour les tailles d'écran moyennes (md) et plus grandes */
    @media (min-width: 768px) {
      .bg {
        padding: 40px;
      }
    }

  `;
      document.head.appendChild(style);



    });
} else if (buttonMandat) {
  document
    .getElementById("ManualInputMandat")
    .addEventListener("click", function () {
      document.getElementById("fileContent").innerHTML = `
  <div class="bg">
      <div class="container h-100">
        <div class="row h-100 justify-content-center align-items-center">
          <div class="col-8">
            <form id="depensesForm" method="POST" action="/depenses"  class="form-container">
                <h2 class="text-center">Saisie Mandat</h2>

                <div class="form-group">
                    <label for="textInput1">Code Portfeuille</label>
                    <input type="text" class="form-control" id="CODE_PORTEF" name="CODE_PORTEF" placeholder="011" required="required">
                </div>



                <div class="form-group">
                    <label for="textInput2">Ordonnateur</label>
                    <input type="text" class="form-control" id="ORD" name="ORD" placeholder="108044" required="required">
                </div>



                <div class="form-group">
                    <label for="textInput3">Gestion</label>
                    <input type="text" class="form-control" id="GESTION" name="GESTION" placeholder="2023" required="required">
                </div>



                <div class="form-group">
                    <label for="textInput4">Code Mandat</label>
                    <input type="text" class="form-control" id="CODE_MANDAT" name="CODE_MANDAT" placeholder="2415" required="required">
                </div>



                <div class="form-group">
                    <label for="textInput5">Date émission</label>
                    <input type="text" class="form-control" id="DT_EMISSION" name="DT_EMISSION" placeholder="13/12/2023" required="required">
                </div>




                <div class="form-group">
                    <label for="textInput6">Statut</label>
                    <input type="text" class="form-control" id="STATUT" name="STATUT" placeholder="REGLE" required="required">
                </div>




                <div class="form-group">
                   <label for="textInput7">Programme</label>
                   <input type="text" class="form-control" id="PROG" name="PROG" placeholder="048" required="required">
                </div>




                <div class="form-group">
                    <label for="textInput8">Sous Programme</label>
                    <input type="text" class="form-control" id="S_PROG" name="S_PROG" placeholder="01" required="required">
                </div>





                <div class="form-group">
                    <label for="textInput10">Action</label>
                    <input type="text" class="form-control" id="ACTION" name="ACTION" placeholder="2046" required="required">
                </div>




                <div class="form-group">
                    <label for="textInput11">Sous Action</label>
                    <input type="text" class="form-control" id="S_ACTION" name="S_ACTION" placeholder="000" required="required">
                </div>




                <div class="form-group">
                    <label for="textInput12">Axe économique</label>
                    <input type="text" class="form-control" id="AXE_ECO" name="AXE_ECO" placeholder="12100" required="required">
                </div>




                <div class="form-group">
                    <label for="textInput13">Disponibilité</label>
                    <input type="text" class="form-control" id="DISPOS" name="DISPOS" placeholder="0" required="required">
                </div>


                <div class="form-group">
                    <label for="textInput13">Total Montant</label>
                    <input type="text" class="form-control" id="MONTANT" name="MONTANT" placeholder="5328797.85" required="required">
                </div>



                <div style="display: flex; justify-content: center; margin-left: auto; margin-right: auto;">
                    <button type="submit" class="btn btn-primary" style="margin-right: 20px;">Envoyer</button>
                    <button type="reset" class="btn btn-secondary">Annuler</button>
                </div>



           </form>
         </div>
       </div>
     </div>
   </div>
  `;

      // Ajouter des styles pour le formulaire
      const style = document.createElement("style");
      style.innerHTML = `
      .bg {
        background-color: #0000;
        padding: 0px;
        border-radius: 10px;
        margin-top: -220px; /* Ajuster la marge supérieure */
      }
      .form-container {
        background-color: #062a4e; 
        border-color: #062a4e;
        color: white;
        padding: 20px;
        border-radius: 10px;
        box-shadow: 0px 10px 10px 10px rgba(173, 216, 230, 0.5);
      }

      /* Styles pour les tailles d'écran moyennes (md) et plus grandes */
      @media (min-width: 768px) {
      .bg {
        padding: 40px;
       }
      }

  `;
      document.head.appendChild(style);
    

  });


} else if (buttonRecette) {
  document
    .getElementById("ManualInputRecettes")
    .addEventListener("click", function () {
      document.getElementById("fileContent").innerHTML = `
<div class="bg">
  <div class="container h-100">
    <div class="row h-100 justify-content-center align-items-center">
      <div class="col-8">
       <form id="RecetteForm" method="POST" action="/Recettes" class="form-container">
          <h2 class="text-center">Saisie Recette</h2>

          <div class="form-group">
              <label for="textInput1">Gestion</label>
              <input type="text" class="form-control" id="GESTION" name="GESTION" placeholder="2024" required="required">
          </div>

        
          <div class="form-group">
             <label for="textInput2">Mois</label>
             <input type="text" class="form-control" id="MOIS" name="MOIS" placeholder="04" required="required">
          </div>



          <div class="form-group">
            <label for="textInput3">Code Compte</label>
            <input type="text" class="form-control" id="CODE_CPT" name="CODE_CPT" placeholder="0000" required="required">
          </div>


          <div class="form-group">
           <label for="textInput4">Libillé Compte</label>
           <input type="text" class="form-control" id="LIB_CPT_G" name="LIB_CPT_G" placeholder="YYYYYYYY" required="required">
          </div>


          <div class="form-group">
           <label for="textInput4">Poste Comptable</label>
           <input type="text" class="form-control" id="POSTE_C" name="POSTE_C" placeholder="10" required="required">
          </div>



          <div class="form-group">
           <label for="textInput11">Total Montant</label>
           <input type="text" class="form-control" id="crédit" name="MT_MOIS" placeholder="10000000" required="required">
          </div>


          <div style="display: flex; justify-content: center; margin-left: auto; margin-right: auto;">
           <button type="submit" class="btn btn-primary" style="margin-right: 20px;">Envoyer</button>
           <button type="reset" class="btn btn-secondary">Annuler</button>
          </div>

    </form>
   </div>
  </div>
 </div>
</div>
  `;

      // Ajouter des styles pour le formulaire
      const style = document.createElement("style");
      style.innerHTML = `
      .bg {
        background-color: #0000;
        padding: 0px;
        border-radius: 10px;
        margin-top: -200px; /* Ajuster la marge supérieure */
      }
      .form-container {
        background-color: #062a4e; 
        border-color: #062a4e;
        color: white;
        padding: 20px;
        border-radius: 10px;
        box-shadow: 0px 10px 10px 10px rgba(173, 216, 230, 0.5);
      }

    /* Styles pour les tailles d'écran moyennes (md) et plus grandes */
    @media (min-width: 768px) {
      .bg {
        padding: 40px;
      }
    }

  `;
      document.head.appendChild(style);

      
      
    });


}else if (buttonDettes) {
  document
    .getElementById("ManualInputDettes")
    .addEventListener("click", function () {
      document.getElementById("fileContent").innerHTML = `
      <div class="bg">
      <div class="container h-100">
       <div class="row h-100 justify-content-center align-items-center">
         <div class="col-8">
          <form id="DetteForm" method="POST" action="/Dettes" class="form-container">
                   <h2 class="text-center">Saisie Dette</h2>
    
              <div class="form-group">
                <label for="textInput2">code ISIN</label>
                <input type="text" class="form-control" id="code_ISIN" name="code_ISIN" placeholder="0000" required="required">
              </div>
    
    
              <div class="form-group">
                <label for="textInput4">date début</label>
                <input type="text" class="form-control" id="date_début" name="date_début" placeholder="JJ/MM/YYYY" required="required">
              </div>
    
              <div class="form-group">
                <label for="selectOption">durée bon</label>
                <select class="form-control" id="Typebon" name="Typebon" required="required">
                  <option value="13_semaines"> 13 semaines</option>
                  <option value="26_semaines"> 26 semaines</option>
                  <option value="1_ans"> 1 ans</option>
                  <option value="2_ans"> 2 ans</option>
                  <option value="3_ans"> 3 ans</option>
                  <option value="5_ans"> 5 ans</option>
                  <option value="7_ans"> 7 ans</option>
                  <option value="10_ans"> 10 ans</option>
                  <option value="15_ans"> 15 ans</option>
                </select>
              </div>
    
              <div class="form-group">
                <label for="textInput4">date d'échéance</label>
                <input type="text" class="form-control" id="date_déchéance" name="date_déchéance" placeholder="JJ/MM/YYYY" required="required">
              </div>
    
              <div class="form-group">
                <label for="textInput4">Code Soumissionaire</label>
                <input type="text" class="form-control" id="Code_Soumissionaire" name="Code_Soumissionaire" placeholder="0000" required="required">
              </div>
    
              <div class="form-group">
                <label for="textInput5">Montant Proposé Par l'état</label>
                <input type="text" class="form-control" id="Montant_Proposé_état" name="Montant_Proposé_état" placeholder="0000000" required="required">
              </div>
    
              <div class="form-group">
                <label for="textInput6">Montant Proposé Par Soumissionaire</label>
                <input type="text" class="form-control" id="Montant_Proposé_Soumissionaire" name="Montant_Proposé_Soumissionaire" placeholder="0000000" required="required">
              </div>
    
              <div class="form-group">
                <label for="textInput7">Montant Adjugé</label>
                <input type="text" class="form-control" id="Montant_Adjugé" name="Montant_Adjugé" placeholder="0000000" required="required">
              </div>
    
              <div class="form-group">
                <label for="textInput8">Montant Coupoun</label>
                <input type="text" class="form-control" id="Montant_Coupoun" name="Montant_Coupoun" placeholder="0000000" required="required">
              </div>
    
              <div style="display: flex; justify-content: center; margin-left: auto; margin-right: auto;">
                 <button type="submit" class="btn btn-primary" style="margin-right: 20px;">Envoyer</button>
                 <button type="reset" class="btn btn-secondary">Annuler</button>
              </div>
    
           </form>
          </div>
         </div>
        </div>
       </div>
  `;

      // Ajouter des styles pour le formulaire
      const style = document.createElement("style");
      style.innerHTML = `
      .bg {
        background-color: #0000;
        padding: 0px;
        border-radius: 10px;
        margin-top: -220px; /* Ajuster la marge supérieure */
      }
      .form-container {
        background-color: #062a4e; 
        border-color: #062a4e;
        color: white;
        padding: 20px;
        border-radius: 10px;
        box-shadow: 0px 10px 10px 10px rgba(173, 216, 230, 0.5);
      }

    /* Styles pour les tailles d'écran moyennes (md) et plus grandes */
    @media (min-width: 768px) {
      .bg {
        padding: 40px;
      }
    }

  `;
      document.head.appendChild(style);

    });
}
