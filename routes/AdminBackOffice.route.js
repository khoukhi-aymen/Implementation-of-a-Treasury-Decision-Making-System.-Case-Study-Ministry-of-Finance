const express = require("express");
const route = express.Router();
const bodyParser = require("body-parser");
const GuardAuth = require("./guardAuth");
const AuthControllerAdminBackOffice = require("../Controllers/AdminBackOffice.Controller");


/********************************  dépenses *********************************/
route.get("/depenses", (req, res, next) => {
  res.render("AdminBackOffice/depenses", { test: "aymen" });
});

// Route POST pour recevoir les colonnes
route.post("/depenses", (req, res) => {
  const contentType = req.headers["content-type"];

  if (contentType.includes("application/json")) {
    const colonnes = req.body.colonnes; // Utilisez la bonne clé
    console.log("Requête AJAX reçue");
    console.log(colonnes);

    // Parcourir chaque propriété de l'objet colonnes
    for (const key in colonnes) {
      if (Object.hasOwnProperty.call(colonnes, key)) {
        const array = colonnes[key]; // Récupérer le tableau correspondant à la propriété key
        const myChar = "\u001F";
        const stringifiedArray = array.join(myChar); // Convertir le tableau en une seule chaîne de caractères séparée par myChar
        //console.log(typeof stringifiedArray);
        //console.log(typeof key);
        console.log(`${key}:`, stringifiedArray);
        AuthControllerAdminBackOffice.ETLFichier(key, stringifiedArray)
          .then((msg) => {
            res.render("AdminBackOffice/depenses");
          })
          .catch((error) => {
            console.error(error);
          });
      }
    }
    res.status(200).json({ message: "Données envoyées avec succès !" });
  } else if (
    contentType.includes("application/x-www-form-urlencoded") ||
    contentType.includes("multipart/form-data")
  ) {
    // Gérer la soumission de formulaire
    console.log("Soumission de formulaire reçue");
    const CodePortefeuille = req.body.CODE_PORTEF;
    const Ordonateur = req.body.ORD;
    const Gestion = req.body.GESTION;
    const CodeMandat = req.body.CODE_MANDAT;
    const DateEmission = req.body.DT_EMISSION;
    const Statut = req.body.STATUT;
    const Programme = req.body.PROG;
    const SousProgramme = req.body.S_PROG;
    const Action = req.body.ACTION;
    const SousAction = req.body.S_ACTION;
    const AxeEconomique = req.body.AXE_ECO;
    const Disponibilité = req.body.DISPOS;
    const TotalMontant = req.body.MONTANT;

    // console.log(Gestion);
    // console.log(Mois);
    // console.log(CodeCompte);
    // console.log(LibCompte);
    // console.log(posteCompatble);
    // console.log(TotalMontant);
    AuthControllerAdminBackOffice.ETLFormulaire(
      CodePortefeuille,
      Ordonateur,
      Gestion,
      CodeMandat,
      DateEmission,
      Statut,
      Programme,
      SousProgramme,
      Action,
      SousAction,
      AxeEconomique,
      Disponibilité,
      TotalMontant,
      "Depenses"
    )
      .then((msg) => {
        console.log(msg)

        res.render("AdminBackOffice/depenses",{msg:msg , test: "aymen"});
      })
      .catch((error) => {
        console.error(error);
      });
    // Traiter formData ici
  } else {
    res.status(400).json({ message: "Content-Type non pris en charge" });
  }
});

/********************************  dépenses *********************************/

/********************************  Credits *********************************/

route.get("/Credits", (req, res, next) => {
  res.render("AdminBackOffice/Credits", { test: "omar" });
});

// Route POST pour recevoir les colonnes
route.post("/Credits", (req, res) => {
  const contentType = req.headers["content-type"];

  if (contentType.includes("application/json")) {
    const colonnes = req.body.colonnes; // Utilisez la bonne clé
    console.log("Requête AJAX reçue");
    console.log(colonnes);

    // Parcourir chaque propriété de l'objet colonnes
    for (const key in colonnes) {
      if (Object.hasOwnProperty.call(colonnes, key)) {
        const array = colonnes[key]; // Récupérer le tableau correspondant à la propriété key
        const myChar = "\u001F";
        const stringifiedArray = array.join(myChar); // Convertir le tableau en une seule chaîne de caractères séparée par myChar
        //console.log(typeof stringifiedArray);
        //console.log(typeof key);
        console.log(`${key}:`, stringifiedArray);
        AuthControllerAdminBackOffice.ETLFichier(key, stringifiedArray)
          .then((msg) => {
            //console.log('Utilisateurs récupérés avec succès:', users);
            res.render("AdminBackOffice/Credits");
          })
          .catch((error) => {
            console.error(error);
          });
      }
    }
    res.status(200).json({ message: "Données envoyées avec succès !" });
  } else if (
    contentType.includes("application/x-www-form-urlencoded") ||
    contentType.includes("multipart/form-data")
  ) {
    // Gérer la soumission de formulaire
    console.log("Soumission de formulaire reçue");
    const Gestion = req.body.GESTION;
    const Mois = req.body.MOIS;
    const Ordonnateur = req.body.ORD;
    const CodePortefeuille = req.body.PORTEF;
    const Programme = req.body.PROG;
    const SousProgramme = req.body.S_PROG;
    const Action = req.body.ACTION;
    const SousAction = req.body.S_ACTION;
    const AxeEconomique = req.body.AXE_ECO;
    const Disponibilité = req.body.DISPOS;
    const TotalCredit = req.body.TOT_CREDIT;
    const TotalDeb = req.body.TOT_DEB;
    const Solde = req.body.SOLDE;

    // console.log(Gestion);
    // console.log(Mois);
    // console.log(CodeCompte);
    // console.log(LibCompte);
    // console.log(posteCompatble);
    // console.log(TotalMontant);
    AuthControllerAdminBackOffice.ETLFormulaire(
      Gestion,
      Mois,
      Ordonnateur,
      CodePortefeuille,
      Programme,
      SousProgramme,
      Action,
      SousAction,
      AxeEconomique,
      Disponibilité,
      TotalCredit,
      TotalDeb,
      Solde,
      "Credits"
    )
      .then((msg) => {
        //console.log('Utilisateurs récupérés avec succès:', users);
        console.log(msg);
        res.render("AdminBackOffice/Credits",{msg:msg , test: "omar"});
      })
      .catch((error) => {
        console.error(error);
      });
    // Traiter formData ici
  } else {
    res.status(400).json({ message: "Content-Type non pris en charge" });
  }
});

/********************************  Credits *********************************/

/********************************  Recetes *********************************/

route.get("/Recettes", (req, res, next) => {
  res.render("AdminBackOffice/Recettes", { test: "alaa" });
});

// Route POST pour recevoir les colonnes
route.post("/Recettes", (req, res) => {

  const contentType = req.headers["content-type"];

  if (contentType.includes("application/json")) {
    const colonnes = req.body.colonnes; // Utilisez la bonne clé
    console.log("Requête AJAX reçue");
    console.log(colonnes);

    // Parcourir chaque propriété de l'objet colonnes
    for (const key in colonnes) {
      if (Object.hasOwnProperty.call(colonnes, key)) {
        const array = colonnes[key]; // Récupérer le tableau correspondant à la propriété key
        const myChar = "\u001F";
        const stringifiedArray = array.join(myChar); // Convertir le tableau en une seule chaîne de caractères séparée par myChar
        //console.log(typeof stringifiedArray);
        //console.log(typeof key);
        console.log(`${key}:`, stringifiedArray);
        AuthControllerAdminBackOffice.ETLFichier(key, stringifiedArray)
          .then((msg) => {
            //console.log('Utilisateurs récupérés avec succès:', users);
            res.render("AdminBackOffice/Recettes");
          })
          .catch((error) => {
            console.error(error);
          });
      }
    }
    res.status(200).json({ message: "Données envoyées avec succès !" });
  } else if (
    contentType.includes("application/x-www-form-urlencoded") ||
    contentType.includes("multipart/form-data")
  ) {
    // Gérer la soumission de formulaire
    console.log("Soumission de formulaire reçue");
    const Gestion = req.body.GESTION;
    const Mois = req.body.MOIS;
    const CodeCompte = req.body.CODE_CPT;
    const LibCompte = req.body.LIB_CPT_G;
    const posteCompatble = req.body.POSTE_C;
    const TotalMontant = req.body.MT_MOIS;

    // console.log(Gestion);
    // console.log(Mois);
    // console.log(CodeCompte);
    // console.log(LibCompte);
    // console.log(posteCompatble);
    // console.log(TotalMontant);
    AuthControllerAdminBackOffice.ETLFormulaire(
      Gestion,
      Mois,
      CodeCompte,
      LibCompte,
      posteCompatble,
      TotalMontant,
      "XXXXXXXX",
      "XXXXXXXX",
      "XXXXXXXX",
      "XXXXXXXX",
      "XXXXXXXX",
      "XXXXXXXX",
      "XXXXXXXX",
      "Recette"
    )
      .then((msg) => {
        //console.log('Utilisateurs récupérés avec succès:', users);
        res.render("AdminBackOffice/Recettes",{msg:msg , test: "alaa"});
      })
      .catch((error) => {
        console.error(error);
      });
    // Traiter formData ici
  } else {
    res.status(400).json({ message: "Content-Type non pris en charge" });
  }
});

/********************************  Recetes *********************************/

/********************************  Dettes *********************************/

route.get("/Dettes", (req, res, next) => {
  res.render("AdminBackOffice/Dettes", { test: "imad" });
});

// Route POST pour recevoir les colonnes
route.post("/Dettes", (req, res) => {

  const contentType = req.headers["content-type"];

  if (contentType.includes("application/json")) {
    // Gérer la requête AJAX
    const colonnes = req.body.colonnes;
    console.log("Requête AJAX reçue");
    console.log(colonnes);

    // Parcourir chaque propriété de l'objet colonnes
    for (const key in colonnes) {
      if (Object.hasOwnProperty.call(colonnes, key)) {
        const array = colonnes[key]; // Récupérer le tableau correspondant à la propriété key
        const myChar = "\u001F";
        const stringifiedArray = array.join(myChar); // Convertir le tableau en une seule chaîne de caractères séparée par myChar
        //console.log(typeof stringifiedArray);
        //console.log(typeof key);
        console.log(`${key}:`, stringifiedArray);
        AuthControllerAdminBackOffice.ETLFichier(key, stringifiedArray)
          .then((msg) => {
            //console.log('Utilisateurs récupérés avec succès:', users);
            res.render("AdminBackOffice/Dettes");
          })
          .catch((error) => {
            console.error(error);
          });
      }
    }
    res.status(200).json({ message: "Données envoyées avec succès !" });



  } else if (
    contentType.includes("application/x-www-form-urlencoded") ||
    contentType.includes("multipart/form-data")
  ) {
    // Gérer la soumission de formulaire
    console.log("Soumission de formulaire reçue");
    const code_ISIN = req.body.code_ISIN;
    const date_début = req.body.date_début;
    const Typebon = req.body.Typebon;
    const date_déchéance = req.body.date_déchéance;
    const Code_Soumissionaire = req.body.Code_Soumissionaire;
    const Montant_Proposé_état = req.body.Montant_Proposé_état;
    const Montant_Proposé_Soumissionaire = req.body.Montant_Proposé_Soumissionaire;
    const Montant_Adjugé = req.body.Montant_Adjugé;
    const Montant_Coupoun = req.body.Montant_Coupoun;

    // console.log(code_ISIN);
    // console.log(date_début);
    // console.log(Typebon);
    // console.log(date_déchéance);
    // console.log(Code_Soumissionaire);
    // console.log(Montant_Proposé_état);
    // console.log(Montant_Proposé_Soumissionaire);
    // console.log(Montant_Adjugé);
    // console.log(Montant_Coupoun);



    AuthControllerAdminBackOffice.ETLFormulaire(
      code_ISIN,
      date_début,
      Typebon,
      date_déchéance,
      Code_Soumissionaire,
      Montant_Proposé_état,
      Montant_Proposé_Soumissionaire,
      Montant_Adjugé,
      Montant_Coupoun,
      "XXXXXXXX",
      "XXXXXXXX",
      "XXXXXXXX",
      "XXXXXXXX",
      "Dette"
    )
      .then((msg) => {
        //console.log('Utilisateurs récupérés avec succès:', users);
        res.render("AdminBackOffice/Dettes",{msg:msg , test: "imad"});
      })
      .catch((error) => {
        console.error(error);
      });
    // Traiter formData ici




  } else {
    res.status(400).json({ message: "Content-Type non pris en charge" });
  }
});




/********************************  Dettes *********************************/

route.get("/tables", (req, res, next) => {
  res.render("AdminBackOffice/Tables", { test: "" });
});
module.exports = route;
