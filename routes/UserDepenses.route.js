const route = require("express").Router();
const body = require("express").urlencoded({ urlencoded: true });
const GuardAuth = require("./guardAuth");
const AuthController = require("../Controllers/UserDepenses.Controller");




route.get("/depensesReelUD", (req, res, next) => {
  res.render("UserDepenses/depensesReel",{objects:'', test: "alaa" });
});


route.get("/depensesPrevuUD", (req, res, next) => {
  res.render("UserDepenses/depensesPrevu",{objects:'' , objectsPrevu:'', test: "faten" });
});


route.get("/CreditsReelUD", (req, res, next) => {
  res.render("UserDepenses/CreditsReel",{objects:'', test: "imad" });
});

route.get("/CreditsPrevuUD", (req, res, next) => {
  res.render("UserDepenses/CreditsPrevu",{objects:'' , objectsPrevu:'', test: "moh" });
});





/*************************************************Requetes SQL**********************************************/


/********************************Crédits Réel******************************/

route.post("/CreditsReelUD", body, (req, res) => {
  const selectedOptions = req.body.selectedOptions;
  const selectedDimensions = req.body.selectedDimensions;
  const selectedMesures = req.body.selectedMesures;
  const year = req.body.selectedDates;
  const birthday = req.body.birthday;


  if (
    selectedOptions.includes("Total_M_D") &&
    selectedDimensions.includes("Portefeuille") &&
    (year || birthday)
  ) {

    let date = "";
    if(year){
      date = year+"-01-01";
    }else if(birthday){
      date = birthday;
    }
    const Portefeuille = "Portefeuille";
    const TMD = "Total_M_D";
    //Total des crédits alloués par portef
    AuthController.getTotalCreditsAllouePourChaquePortefeuilleReel(Portefeuille, TMD,date)
      .then((objects) => {
        console.log("objects", objects);
        res.render("UserDepenses/CreditsReel",{ objects: objects, test: "imad"});
      })
      .catch((error) => {
        console.error("Error:", error);
      });


  }else if (
    selectedOptions.includes("Total_M_D") &&
    selectedDimensions.includes("Prg") &&
    (year || birthday)
  ) {

    let date = "";
    if(year){
      date = year+"-01-01";
    }else if(birthday){
      date = birthday;
    }
    const Programme = "Programme";
    const TMD = "Total_M_D";
    //Total des crédits alloués par portef
    AuthController.getTotalCreditsAllouePourChaqueProgrammeReel(Programme, TMD,date)
      .then((objects) => {
        console.log("objects", objects);
        res.render("UserDepenses/CreditsReel",{ objects: objects, test: "imad"});
      })
      .catch((error) => {
        console.error("Error:", error);
      });



  } else if (
    selectedOptions.includes("Total_M_D") &&
    selectedDimensions.includes("SousProgramme")&&
    (year || birthday)
  ) {

    let date = "";
    if(year){
      date = year+"-01-01";
    }else if(birthday){
      date = birthday;
    }
    const SousProgramme = "SousProgramme";
    const TMD = "Total_M_D";
    //Total des crédits alloués par portef
    AuthController.getTotalCreditsAllouePourChaqueSousProgrammeReel(SousProgramme, TMD,date)
      .then((objects) => {
        console.log("objects", objects);
        res.render("UserDepenses/CreditsReel",{ objects: objects, test: "imad"});
      })
      .catch((error) => {
        console.error("Error:", error);
      });



  } else if (
    selectedOptions.includes("Total_M_D") &&
    selectedDimensions.includes("Titre")&&
    (year || birthday)
  ) {

    let date = "";
    if(year){
      date = year+"-01-01";
    }else if(birthday){
      date = birthday;
    }
    const Titre = "Titre";
    const TMD = "Total_M_D";
    //Total des crédits alloués par portef
    AuthController.getTotalCreditsAllouePourChaqueTitreReel(Titre, TMD,date)
      .then((objects) => {
        console.log("objects", objects);
        res.render("UserDepenses/CreditsReel",{ objects: objects, test: "imad"});
      })
      .catch((error) => {
        console.error("Error:", error);
      });


  } else if (
    selectedOptions.includes("Total_M_D") &&
    selectedDimensions.includes("Catégorie")&&
    (year || birthday)
  ) {

    let date = "";
    if(year){
      date = year+"-01-01";
    }else if(birthday){
      date = birthday;
    }
    const Categorie = "Catégorie";
    const TMD = "Total_M_D";
    //Total des crédits alloués par portef
    AuthController.getTotalCreditsAllouePourChaqueCategorieReel(Categorie, TMD,date)
      .then((objects) => {
        console.log("objects", objects);
        res.render("UserDepenses/CreditsReel",{ objects: objects, test: "imad"});
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  } 
});



/********************************Crédits Réel******************************/




/********************************Crédits prévu******************************/

route.post("/CreditsPrevuUD", body, (req, res) => {
  const selectedOptions = req.body.selectedOptions;
  const selectedDimensions = req.body.selectedDimensions;
  const selectedMesures = req.body.selectedMesures;
  const year = req.body.selectedDates;
  const birthday = req.body.birthday;
  


  if (
    (selectedOptions.includes("Total_M_D") &&
    selectedOptions.includes("Prevision_mensuelle") &&
    selectedDimensions.includes("Portefeuille") &&
    (birthday)) || 
    (selectedOptions.includes("Total_M_D") &&
    selectedOptions.includes("Prevision_annuelle") &&
    selectedDimensions.includes("Portefeuille") &&
    (year))
  ) {

    let date = "";
    let date_prevu = "";
    if(year){
      date = year+"-01-01";
      date_prevu ="Prevision_annuelle";
    }else if(birthday){
      date = birthday;
      date_prevu ="Prevision_mensuelle";
    }
    const Portefeuille = "Portefeuille";
    const TMD = "Total_M_D";
    
    // Total des crédits alloués par portef
    AuthController.getTotalCreditsAllouePourChaquePortefeuillePrevu(Portefeuille, TMD, date,date_prevu)
      .then((Allobjects) => {
        //console.log("objects", objects);

        // Stocker chaque tableau d'objets dans une variable distincte
        const objectsArray1 = Allobjects[0];
        const objectsArray2 = Allobjects[1];

        console.log(objectsArray1)
        console.log(objectsArray2)

        // Envoyer chaque tableau à la vue
        res.render("UserDepenses/CreditsPrevu", { objects: objectsArray1, objectsPrevu: objectsArray2, test: "moh" });
      })
      .catch((error) => {
        console.error("Error:", error);
      });

  }else if (
    (selectedOptions.includes("Total_M_D") &&
    selectedOptions.includes("Prevision_mensuelle") &&
    selectedDimensions.includes("Prg") &&
    (birthday)) || 
    (selectedOptions.includes("Total_M_D") &&
    selectedOptions.includes("Prevision_annuelle") &&
    selectedDimensions.includes("Prg") &&
    (year))
  ) {

    let date = "";
    let date_prevu = "";
    if(year){
      date = year+"-01-01";
      date_prevu ="Prevision_annuelle";
    }else if(birthday){
      date = birthday;
      date_prevu ="Prevision_mensuelle";
    }
    const Programme = "Programme";
    const TMD = "Total_M_D";
    
    // Total des crédits alloués par Categorie
    AuthController.getTotalCreditsAllouePourChaqueProgrammePrevu(Programme, TMD, date,date_prevu)
      .then((Allobjects) => {
        //console.log("objects", objects);

        // Stocker chaque tableau d'objets dans une variable distincte
        const objectsArray1 = Allobjects[0];
        const objectsArray2 = Allobjects[1];

        console.log(objectsArray1)
        console.log(objectsArray2)

        // Envoyer chaque tableau à la vue
        res.render("UserDepenses/CreditsPrevu", { objects: objectsArray1, objectsPrevu: objectsArray2, test: "moh" });
      })
      .catch((error) => {
        console.error("Error:", error);
      });



   }else if (
    (selectedOptions.includes("Total_M_D") &&
    selectedOptions.includes("Prevision_mensuelle") &&
    selectedDimensions.includes("SousProgramme") &&
    (birthday)) || 
    (selectedOptions.includes("Total_M_D") &&
    selectedOptions.includes("Prevision_annuelle") &&
    selectedDimensions.includes("SousProgramme") &&
    (year))
  ) {

    let date = "";
    let date_prevu = "";
    if(year){
      date = year+"-01-01";
      date_prevu ="Prevision_annuelle";
    }else if(birthday){
      date = birthday;
      date_prevu ="Prevision_mensuelle";
    }
    const SousProgramme = "SousProgramme";
    const TMD = "Total_M_D";
    
    // Total des crédits alloués par Categorie
    AuthController.getTotalCreditsAllouePourChaqueSousProgrammePrevu(SousProgramme, TMD, date,date_prevu)
      .then((Allobjects) => {
        //console.log("objects", objects);

        // Stocker chaque tableau d'objets dans une variable distincte
        const objectsArray1 = Allobjects[0];
        const objectsArray2 = Allobjects[1];

        console.log(objectsArray1)
        console.log(objectsArray2)

        // Envoyer chaque tableau à la vue
        res.render("UserDepenses/CreditsPrevu", { objects: objectsArray1, objectsPrevu: objectsArray2, test: "moh" });
      })
      .catch((error) => {
        console.error("Error:", error);
      });



   }else if (
    (selectedOptions.includes("Total_M_D") &&
    selectedOptions.includes("Prevision_mensuelle") &&
    selectedDimensions.includes("Titre") &&
    (birthday)) || 
    (selectedOptions.includes("Total_M_D") &&
    selectedOptions.includes("Prevision_annuelle") &&
    selectedDimensions.includes("Titre") &&
    (year))
  ) {

    let date = "";
    let date_prevu = "";
    if(year){
      date = year+"-01-01";
      date_prevu ="Prevision_annuelle";
    }else if(birthday){
      date = birthday;
      date_prevu ="Prevision_mensuelle";
    }
    const Titre = "Titre";
    const TMD = "Total_M_D";
    
    // Total des crédits alloués par Categorie
    AuthController.getTotalCreditsAllouePourChaqueTitrePrevu(Titre, TMD, date,date_prevu)
      .then((Allobjects) => {
        //console.log("objects", objects);

        // Stocker chaque tableau d'objets dans une variable distincte
        const objectsArray1 = Allobjects[0];
        const objectsArray2 = Allobjects[1];

        console.log(objectsArray1)
        console.log(objectsArray2)

        // Envoyer chaque tableau à la vue
        res.render("UserDepenses/CreditsPrevu", { objects: objectsArray1, objectsPrevu: objectsArray2, test: "moh" });
      })
      .catch((error) => {
        console.error("Error:", error);
      });



   }else if (
    (selectedOptions.includes("Total_M_D") &&
    selectedOptions.includes("Prevision_mensuelle") &&
    selectedDimensions.includes("Catégorie") &&
    (birthday)) || 
    (selectedOptions.includes("Total_M_D") &&
    selectedOptions.includes("Prevision_annuelle") &&
    selectedDimensions.includes("Catégorie") &&
    (year))
  ) {

    let date = "";
    let date_prevu = "";
    if(year){
      date = year+"-01-01";
      date_prevu ="Prevision_annuelle";
    }else if(birthday){
      date = birthday;
      date_prevu ="Prevision_mensuelle";
    }
    const Categorie = "Catégorie";
    const TMD = "Total_M_D";
    
    // Total des crédits alloués par Categorie
    AuthController.getTotalCreditsAllouePourChaqueCategoriePrevu(Categorie, TMD, date,date_prevu)
      .then((Allobjects) => {
        //console.log("objects", objects);

        // Stocker chaque tableau d'objets dans une variable distincte
        const objectsArray1 = Allobjects[0];
        const objectsArray2 = Allobjects[1];

        console.log(objectsArray1)
        console.log(objectsArray2)

        // Envoyer chaque tableau à la vue
        res.render("UserDepenses/CreditsPrevu", { objects: objectsArray1, objectsPrevu: objectsArray2, test: "moh" });
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  } 
});



/********************************Crédits prévu******************************/






/********************************Dépenses Réel******************************/


route.post("/depensesReelUD", body, (req, res) => {
  const selectedOptions = req.body.selectedOptions;
  const selectedDimensions = req.body.selectedDimensions;
  const selectedMesures = req.body.selectedMesures;
  const year = req.body.selectedDates;
  const birthday = req.body.birthday;



  if (
    selectedOptions.includes("b") &&
    selectedDimensions.includes("Portefeuille") &&
    (year || birthday)
  ) {
    let date = "";
    if(year){
      date = year+"-01-01";
    }else if(birthday){
      date = birthday;
    }
    const Portefeuille = "Portefeuille";
    const TMD = "Total_M_dépense";
    //Total des crédits alloués par portef
    AuthController.getTotalMontantdepensePourChaquePortefeuilleReel(Portefeuille, TMD,date)
      .then((objects) => {
        console.log("objects", objects);
        res.render("UserDepenses/depensesReel",{ objects: objects, test: "alaa"});
      })
      .catch((error) => {
        console.error("Error:", error);
      });


  }else if (
    selectedOptions.includes("b") &&
    selectedDimensions.includes("Prg") &&
    (year || birthday)
  ) {

    let date = "";
    if(year){
      date = year+"-01-01";
    }else if(birthday){
      date = birthday;
    }
    const Programme = "Programme";
    const TMD = "Total_M_dépense";
    //Total des crédits alloués par portef
    AuthController.getTotalMontantdepensePourChaqueProgrammeReel(Programme, TMD,date)
      .then((objects) => {
        console.log("objects", objects);
        res.render("UserDepenses/depensesReel",{ objects: objects, test: "alaa"});
      })
      .catch((error) => {
        console.error("Error:", error);
      });



  } else if (
    selectedOptions.includes("b") &&
    selectedDimensions.includes("SousProgramme") &&
    (year || birthday)
  ) {

    let date = "";
    if(year){
      date = year+"-01-01";
    }else if(birthday){
      date = birthday;
    }
    const SousProgramme = "SousProgramme";
    const TMD = "Total_M_dépense";
    //Total des crédits alloués par portef
    AuthController.getTotalMontantdepensePourChaqueSousProgrammeReel(SousProgramme, TMD,date)
      .then((objects) => {
        console.log("objects", objects);
        res.render("UserDepenses/depensesReel",{ objects: objects, test: "alaa"});
      })
      .catch((error) => {
        console.error("Error:", error);
      });



  } else if (
    selectedOptions.includes("b") &&
    selectedDimensions.includes("Titre") &&
    (year || birthday)
  ) {

    let date = "";
    if(year){
      date = year+"-01-01";
    }else if(birthday){
      date = birthday;
    }
    const Titre = "Titre";
    const TMD = "Total_M_dépense";
    //Total des crédits alloués par portef
    AuthController.getTotalMontantdepensePourChaqueTitreReel(Titre, TMD, date)
      .then((objects) => {
        console.log("objects", objects);
        res.render("UserDepenses/depensesReel",{ objects: objects, test: "alaa"});
      })
      .catch((error) => {
        console.error("Error:", error);
      });


  } else if (
    selectedOptions.includes("b") &&
    selectedDimensions.includes("Catégorie") &&
    (year || birthday)
  ) {

    let date = "";
    if(year){
      date = year+"-01-01";
    }else if(birthday){
      date = birthday;
    }
    const Categorie = "Catégorie";
    const TMD = "Total_M_dépense";
    //Total des crédits alloués par portef
    AuthController.getTotalMontantdepensePourChaqueCategorieReel(Categorie, TMD,date)
      .then((objects) => {
        console.log("objects", objects);
        res.render("UserDepenses/depensesReel",{ objects: objects, test: "alaa"});
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  }
});

/********************************Dépenses Réel******************************/


/********************************Dépenses prévu******************************/

route.post("/depensesPrevuUD", body, (req, res) => {
  const selectedOptions = req.body.selectedOptions;
  const selectedDimensions = req.body.selectedDimensions;
  const selectedMesures = req.body.selectedMesures;
  const year = req.body.selectedDates;
  const birthday = req.body.birthday;

  if (
    (selectedOptions.includes("b") &&
    selectedOptions.includes("Prevision_mensuelle") &&
    selectedDimensions.includes("Portefeuille") &&
    (birthday)) || 
    (selectedOptions.includes("b") &&
    selectedOptions.includes("Prevision_annuelle") &&
    selectedDimensions.includes("Portefeuille") &&
    (year))
  ) {

    let date = "";
    let date_prevu = "";
    if(year){
      date = year+"-01-01";
      date_prevu ="Prevision_annuelle";
    }else if(birthday){
      date = birthday;
      date_prevu ="Prevision_mensuelle";
    }

    const Portefeuille = "Portefeuille";
    const TMD = "Total_M_A_dépense";
    
    // Total des crédits alloués par portef
    AuthController.getTotalMontantdepensePourChaquePortefeuillePrevu(Portefeuille, TMD, date,date_prevu)
      .then((Allobjects) => {
        //console.log("objects", objects);

        // Stocker chaque tableau d'objets dans une variable distincte
        const objectsArray1 = Allobjects[0];
        const objectsArray2 = Allobjects[1];

        console.log(objectsArray1)
        console.log(objectsArray2)

        // Envoyer chaque tableau à la vue
        res.render("UserDepenses/depensesPrevu", { objects: objectsArray1, objectsPrevu: objectsArray2, test: "faten" });
      })
      .catch((error) => {
        console.error("Error:", error);
      });

  }else if (
    (selectedOptions.includes("b") &&
    selectedOptions.includes("Prevision_mensuelle") &&
    selectedDimensions.includes("Prg") &&
    (birthday)) || 
    (selectedOptions.includes("b") &&
    selectedOptions.includes("Prevision_annuelle") &&
    selectedDimensions.includes("Prg") &&
    (year))
  ) {

    let date = "";
    let date_prevu = "";
    if(year){
      date = year+"-01-01";
      date_prevu ="Prevision_annuelle";
    }else if(birthday){
      date = birthday;
      date_prevu ="Prevision_mensuelle";
    }
    console.log("hello")
    const Programme = "Programme";
    const TMD = "Total_M_A_dépense";
    
    // Total des crédits alloués par Categorie
    AuthController.getTotalMontantdepensePourChaqueProgrammePrevu(Programme, TMD, date,date_prevu)
      .then((Allobjects) => {
        //console.log("objects", objects);

        // Stocker chaque tableau d'objets dans une variable distincte
        const objectsArray1 = Allobjects[0];
        const objectsArray2 = Allobjects[1];

        console.log(objectsArray1)
        console.log(objectsArray2)

        // Envoyer chaque tableau à la vue
        res.render("UserDepenses/depensesPrevu", { objects: objectsArray1, objectsPrevu: objectsArray2, test: "faten" });
      })
      .catch((error) => {
        console.error("Error:", error);
      });



   } else if (
    (selectedOptions.includes("b") &&
    selectedOptions.includes("Prevision_mensuelle") &&
    selectedDimensions.includes("SousProgramme") &&
    (birthday)) || 
    (selectedOptions.includes("b") &&
    selectedOptions.includes("Prevision_annuelle") &&
    selectedDimensions.includes("SousProgramme") &&
    (year))
  ) {

    let date = "";
    let date_prevu = "";
    if(year){
      date = year+"-01-01";
      date_prevu ="Prevision_annuelle";
    }else if(birthday){
      date = birthday;
      date_prevu ="Prevision_mensuelle";
    }
    const SousProgramme = "SousProgramme";
    const TMD = "Total_M_A_dépense";
    
    // Total des crédits alloués par Categorie
    AuthController.getTotalMontantdepensePourChaqueSousProgrammePrevu(SousProgramme, TMD, date,date_prevu)
      .then((Allobjects) => {
        //console.log("objects", objects);

        // Stocker chaque tableau d'objets dans une variable distincte
        const objectsArray1 = Allobjects[0];
        const objectsArray2 = Allobjects[1];

        console.log(objectsArray1)
        console.log(objectsArray2)

        // Envoyer chaque tableau à la vue
        res.render("UserDepenses/depensesPrevu", { objects: objectsArray1, objectsPrevu: objectsArray2, test: "faten" });
      })
      .catch((error) => {
        console.error("Error:", error);
      });



   }else if (
    (selectedOptions.includes("b") &&
    selectedOptions.includes("Prevision_mensuelle") &&
    selectedDimensions.includes("Titre") &&
    (birthday)) || 
    (selectedOptions.includes("b") &&
    selectedOptions.includes("Prevision_annuelle") &&
    selectedDimensions.includes("Titre") &&
    (year))
  ) {

    let date = "";
    let date_prevu = "";
    if(year){
      date = year+"-01-01";
      date_prevu ="Prevision_annuelle";
    }else if(birthday){
      date = birthday;
      date_prevu ="Prevision_mensuelle";
    }
    const Titre = "Titre";
    const TMD = "Total_M_A_dépense";
    
    // Total des crédits alloués par Categorie
    AuthController.getTotalMontantdepensePourChaqueTitrePrevu(Titre, TMD, date,date_prevu)
      .then((Allobjects) => {
        //console.log("objects", objects);

        // Stocker chaque tableau d'objets dans une variable distincte
        const objectsArray1 = Allobjects[0];
        const objectsArray2 = Allobjects[1];

        console.log(objectsArray1)
        console.log(objectsArray2)

        // Envoyer chaque tableau à la vue
        res.render("UserDepenses/depensesPrevu", { objects: objectsArray1, objectsPrevu: objectsArray2, test: "faten" });
      })
      .catch((error) => {
        console.error("Error:", error);
      });



   }else if (
    (selectedOptions.includes("b") &&
    selectedOptions.includes("Prevision_mensuelle") &&
    selectedDimensions.includes("Catégorie") &&
    (birthday)) || 
    (selectedOptions.includes("b") &&
    selectedOptions.includes("Prevision_annuelle") &&
    selectedDimensions.includes("Catégorie") &&
    (year))
  ) {

    let date = "";
    let date_prevu = "";
    if(year){
      date = year+"-01-01";
      date_prevu ="Prevision_annuelle";
    }else if(birthday){
      date = birthday;
      date_prevu ="Prevision_mensuelle";
    }
    const Categorie = "Catégorie";
    const TMD = "Total_M_A_dépense";
    
    // Total des crédits alloués par Categorie
    AuthController.getTotalMontantdepensePourChaqueCategoriePrevu(Categorie, TMD, date,date_prevu)
      .then((Allobjects) => {
        //console.log("objects", objects);

        // Stocker chaque tableau d'objets dans une variable distincte
        const objectsArray1 = Allobjects[0];
        const objectsArray2 = Allobjects[1];

        console.log(objectsArray1)
        console.log(objectsArray2)

        // Envoyer chaque tableau à la vue
        res.render("UserDepenses/depensesPrevu", { objects: objectsArray1, objectsPrevu: objectsArray2, test: "faten" });
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  } 
});



/********************************Dépneses prévu******************************/











module.exports = route;
