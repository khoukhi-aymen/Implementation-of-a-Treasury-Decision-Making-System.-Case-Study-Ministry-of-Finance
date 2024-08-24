const route = require("express").Router();
const body = require("express").urlencoded({ urlencoded: true });
const GuardAuth = require("./guardAuth");
const AuthController = require("../Controllers/AdminFrontOffice.Controller");

route.get("/dashbordFOFF", (req, res, next) => {
  res.render("AdminFrontOffice/dashbord", {CreditsActuals:'',DepensesActuals:'',RecettesActuels:'',TresorActuals:'', test: "omar",alertMessage:'' });
});

route.get("/depensesReel", (req, res, next) => {
  res.render("AdminFrontOffice/depensesReel", {objects:'', test: "alaa" });
});

route.get("/depensesPrevu", (req, res, next) => {
  res.render("AdminFrontOffice/depensesPrevu", {objects:'' , objectsPrevu:'', test: "faten" });
});

route.get("/RecettesReel", (req, res, next) => {
  res.render("AdminFrontOffice/RecettesReel", {objects:'' , test: "aymene" });
});

route.get("/RecettesPrevu", (req, res, next) => {
  res.render("AdminFrontOffice/RecettesPrevu", {objects:'' , objectsPrevu:'', test: "fouzia" });
});

route.get("/CreditsReel", (req, res, next) => {
  res.render("AdminFrontOffice/CreditsReel", {objects:'', test: "imad" });
});

route.get("/CreditsPrevu", (req, res, next) => {
  res.render("AdminFrontOffice/CreditsPrevu", {objects:'' , objectsPrevu:'', test: "moh" });
});

route.get("/DettesReel", (req, res, next) => {
  res.render("AdminFrontOffice/DettesReel", {objects:'', test: "said" });
});

route.get("/DettesPrevu", (req, res, next) => {
  res.render("AdminFrontOffice/DettesPrevu", {objects:'' , objectsPrevu:'', test: "ahmed" });
});

//***************************************gestion users**************************************** */

route.get("/users", (req, res, next) => {
  // Exemple d'utilisation de la fonction GetAllUsersFromEntropot avec une promesse
  AuthController.GetAllUsersFromEntropot()
    .then((users) => {
      //console.log('Utilisateurs récupérés avec succès:', users);
      res.render("AdminFrontOffice/users", { users: users, test: "" });
    })
    .catch((error) => {
      console.error("Erreur lors de la récupération des utilisateurs:", error);
    });
});

/********************************************delete user******************************************** */
route.get("/deleteUser/:ID", (req, res, next) => {
  let id = req.params.ID;
  // Exemple d'utilisation de la fonction GetAllUsersFromEntropot avec une promesse
  AuthController.deleteUserFromEntropot(id)
    .then((msj) => {
      console.log(msj);
      res.redirect("/users");
    })
    .catch((error) => {
      console.error(error);
    });
});

/****************************************update user au user haut niveau***************************** */

route.get(
  "/updateRoleToUserHautNiveau/:ID/:user_haut_niveau",
  (req, res, next) => {
    let id = req.params.ID;
    let userHniveau = req.params.user_haut_niveau;
    AuthController.updateUSER_AU_user_haut_niveau(id, userHniveau)
      .then((msj) => {
        console.log(msj);
        res.redirect("/users");
      })
      .catch((error) => {
        console.error(error);
      });
  }
);

/****************************************update user au user dépenses***************************** */

route.get("/updateRoleToUserDepenses/:ID/:userDepenses", (req, res, next) => {
  let id = req.params.ID;
  let userDepenses = req.params.userDepenses;
  AuthController.updateUSER_AU_user_Dépenses(id, userDepenses)
    .then((msj) => {
      console.log(msj);
      res.redirect("/users");
    })
    .catch((error) => {
      console.error(error);
    });
});

/****************************************update user to Admin Back-Office***************************** */

route.get(
  "/updateRoleToAminBackOffice/:ID/:AdminBackOffice",
  (req, res, next) => {
    let id = req.params.ID;
    let AdminBackOffice = req.params.AdminBackOffice;
    AuthController.updateUSER_AU_Admin_Back_Office(id, AdminBackOffice)
      .then((msj) => {
        console.log(msj);
        res.redirect("/users");
      })
      .catch((error) => {
        console.error(error);
      });
  }
);

/****************************************update user to Admin Front-Office***************************** */

route.get(
  "/updateRoleToAminFrontOffice/:ID/:AdminFrontOffice",
  (req, res, next) => {
    let id = req.params.ID;
    let AdminFrontOffice = req.params.AdminFrontOffice;
    AuthController.updateUSER_AU_Admin_Front_Office(id, AdminFrontOffice)
      .then((msj) => {
        console.log(msj);
        res.redirect("/users");
      })
      .catch((error) => {
        console.error(error);
      });
  }
);

/****************************************update user to user financement***************************** */

route.get(
  "/updateRoleToUserFinancement/:ID/:UserFinancement",
  (req, res, next) => {
    let id = req.params.ID;
    let UserFinancement = req.params.UserFinancement;
    AuthController.updateUSER_AU_User_Financement(id, UserFinancement)
      .then((msj) => {
        console.log(msj);
        res.redirect("/users");
      })
      .catch((error) => {
        console.error(error);
      });
  }
);

/****************************************update user to user recettes***************************** */

route.get("/updateRoleToUserRecettes/:ID/:UserRecettes", (req, res, next) => {
  let id = req.params.ID;
  let UserRecettes = req.params.UserRecettes;
  AuthController.updateUSER_AU_User_Recettes(id, UserRecettes)
    .then((msj) => {
      console.log(msj);
      res.redirect("/users");
    })
    .catch((error) => {
      console.error(error);
    });
});

/*************************************************Requetes SQL**********************************************/

/********************************Crédits Réel******************************/

route.post("/CreditsReel", body, (req, res) => {
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
        res.render("AdminFrontOffice/CreditsReel",{ objects: objects, test: "imad"});
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
        res.render("AdminFrontOffice/CreditsReel",{ objects: objects, test: "imad"});
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
        res.render("AdminFrontOffice/CreditsReel",{ objects: objects, test: "imad"});
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
        res.render("AdminFrontOffice/CreditsReel",{ objects: objects, test: "imad"});
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
        res.render("AdminFrontOffice/CreditsReel",{ objects: objects, test: "imad"});
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  } 
});



/********************************Crédits Réel******************************/




/********************************Crédits prévu******************************/

route.post("/CreditsPrevu", body, (req, res) => {
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
        res.render("AdminFrontOffice/CreditsPrevu", { objects: objectsArray1, objectsPrevu: objectsArray2, test: "moh" });
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
        res.render("AdminFrontOffice/CreditsPrevu", { objects: objectsArray1, objectsPrevu: objectsArray2, test: "moh" });
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
        res.render("AdminFrontOffice/CreditsPrevu", { objects: objectsArray1, objectsPrevu: objectsArray2, test: "moh" });
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
        res.render("AdminFrontOffice/CreditsPrevu", { objects: objectsArray1, objectsPrevu: objectsArray2, test: "moh" });
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
        res.render("AdminFrontOffice/CreditsPrevu", { objects: objectsArray1, objectsPrevu: objectsArray2, test: "moh" });
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  } 
});



/********************************Crédits prévu******************************/






/********************************Dépenses Réel******************************/


route.post("/depensesReel", body, (req, res) => {
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
        res.render("AdminFrontOffice/depensesReel",{ objects: objects, test: "alaa"});
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
        res.render("AdminFrontOffice/depensesReel",{ objects: objects, test: "alaa"});
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
        res.render("AdminFrontOffice/depensesReel",{ objects: objects, test: "alaa"});
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
        res.render("AdminFrontOffice/depensesReel",{ objects: objects, test: "alaa"});
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
        res.render("AdminFrontOffice/depensesReel",{ objects: objects, test: "alaa"});
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  }
});

/********************************Dépenses Réel******************************/


/********************************Dépenses prévu******************************/

route.post("/depensesPrevu", body, (req, res) => {
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
        res.render("AdminFrontOffice/depensesPrevu", { objects: objectsArray1, objectsPrevu: objectsArray2, test: "faten" });
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
        res.render("AdminFrontOffice/depensesPrevu", { objects: objectsArray1, objectsPrevu: objectsArray2, test: "faten" });
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
        res.render("AdminFrontOffice/depensesPrevu", { objects: objectsArray1, objectsPrevu: objectsArray2, test: "faten" });
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
        res.render("AdminFrontOffice/depensesPrevu", { objects: objectsArray1, objectsPrevu: objectsArray2, test: "faten" });
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
        res.render("AdminFrontOffice/depensesPrevu", { objects: objectsArray1, objectsPrevu: objectsArray2, test: "faten" });
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  } 
});



/********************************Dépneses prévu******************************/



/******************************* Recette Réel *******************************/


route.post("/RecetteReel", body, (req, res) => {
  const selectedOptions = req.body.selectedOptions;
  const selectedDimensions = req.body.selectedDimensions;
  const selectedMesures = req.body.selectedMesures;
  const year = req.body.selectedDates;
  const birthday = req.body.birthday;


  if (
    selectedOptions.includes("Total_Solde") &&
    selectedDimensions.includes("Compte") && 
    (year || birthday)
  ) {

    let date = "";
    if(year){
      date = year+"-01-01";
    }else if(birthday){
      date = birthday;
    }
    const Compte = "Compte";
    const TMD = "Total_Solde";
    AuthController.getTotalMontantPourChaqueCompteReel(Compte, TMD,date)
      .then((objects) => {
        console.log("objects", objects);
        res.render("AdminFrontOffice/RecettesReel",{ objects: objects, test: "aymene"});
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  

  }else  if (
    selectedOptions.includes("Total_Solde") &&
    selectedDimensions.includes("Poste_Comptable") && 
    (year || birthday)
  ) {
    let date = "";
    if(year){
      date = year+"-01-01";
    }else if(birthday){
      date = birthday;
    }
    const PosteComptable = "Poste_Comptable";
    const TMD = "Total_Solde";
    AuthController.getTotalMontantPourChaquePosteComptableReel(PosteComptable, TMD,date)
      .then((objects) => {
        console.log("objects", objects);
        res.render("AdminFrontOffice/RecettesReel",{ objects: objects, test: "aymene"});
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  
  }
});



/******************************* Recette Réel *******************************/


/******************************* Recette Prévu ******************************/
route.post("/RecettePrevu", body, (req, res) => {
  const selectedOptions = req.body.selectedOptions;
  const selectedDimensions = req.body.selectedDimensions;
  const selectedMesures = req.body.selectedMesures;
  const year = req.body.selectedDates;
  const birthday = req.body.birthday;
  


  if (
    (selectedOptions.includes("Total_Solde") &&
    selectedOptions.includes("Prevision_mensuelle") &&
    selectedDimensions.includes("Compte") &&
    (birthday)) || 
    (selectedOptions.includes("Total_Solde") &&
    selectedOptions.includes("Prevision_annuelle") &&
    selectedDimensions.includes("Compte") &&
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
    const Compte = "Compte";
    const TMD = "Total_Solde";
    
    // Total des crédits alloués par portef
    AuthController.getTotalMontantPourChaqueComptePrevu(Compte, TMD, date,date_prevu)
      .then((Allobjects) => {
        //console.log("objects", objects);

        // Stocker chaque tableau d'objets dans une variable distincte
        const objectsArray1 = Allobjects[0];
        const objectsArray2 = Allobjects[1];

        console.log(objectsArray1)
        console.log(objectsArray2)

        // Envoyer chaque tableau à la vue
        res.render("AdminFrontOffice/RecettesPrevu", { objects: objectsArray1, objectsPrevu: objectsArray2, test: "fouzia" });
      })
      .catch((error) => {
        console.error("Error:", error);
      });

  }else if (
    (selectedOptions.includes("Total_Solde") &&
    selectedOptions.includes("Prevision_mensuelle") &&
    selectedDimensions.includes("Poste_Comptable") &&
    (birthday)) || 
    (selectedOptions.includes("Total_Solde") &&
    selectedOptions.includes("Prevision_annuelle") &&
    selectedDimensions.includes("Poste_Comptable") &&
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
    const PosteComptable = "Poste_Comptable";
    const TMD = "Total_Solde";
    
    // Total des crédits alloués par portef
    AuthController.getTotalMontantPourChaquePosteComptablePrevu(PosteComptable, TMD, date,date_prevu)
      .then((Allobjects) => {
        //console.log("objects", objects);

        // Stocker chaque tableau d'objets dans une variable distincte
        const objectsArray1 = Allobjects[0];
        const objectsArray2 = Allobjects[1];

        console.log(objectsArray1)
        console.log(objectsArray2)

        // Envoyer chaque tableau à la vue
        res.render("AdminFrontOffice/RecettesPrevu", { objects: objectsArray1, objectsPrevu: objectsArray2, test: "fouzia" });
      })
      .catch((error) => {
        console.error("Error:", error);
      });

  }
});


/******************************* Recette Prévu ******************************/





/********************************* Dette Réel *******************************/


route.post("/DettesReel", body, (req, res) => {
  const selectedOptions = req.body.selectedOptions;
  const selectedDimensions = req.body.selectedDimensions;
  const selectedMesures = req.body.selectedMesures;
  const year = req.body.selectedDates;
  const birthday = req.body.birthday;

  if (
    selectedOptions.includes("Total_M_R") &&
    selectedDimensions.includes("Soumissionaire") &&
    (year || birthday)
  ) {

    let date = "";
    if(year){
      date = year+"-01-01";
    }else if(birthday){
      date = birthday;
    }
    
    const Soumissionaire = "Soumissionaire";
    const TMD = "Total_M_R";
    AuthController.getTotalMontantRembourcePourChaqueSoumissionaireReel(Soumissionaire, TMD,date)
      .then((objects) => {
        console.log("objects", objects);
        res.render("AdminFrontOffice/DettesReel",{ objects: objects, test: "said"});
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  
  } else if (
    selectedOptions.includes("Total_M_R") &&
    selectedDimensions.includes("Titre")  &&
    (year || birthday)
  ) {

    let date = "";
    if(year){
      date = year+"-01-01";
    }else if(birthday){
      date = birthday;
    }

    const TMD = "Total_M_R";
    const Titre = "Titre";
    AuthController.getTotalMontantRembourcePourChaqueTitreReel(Titre, TMD,date)
      .then((objects) => {
        console.log("objects", objects);
        res.render("AdminFrontOffice/DettesReel",{ objects: objects, test: "said"});
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  
  } 
});



/********************************* Dette Réel *******************************/




/******************************* Dette Prévu ******************************/
route.post("/DettesPrevu", body, (req, res) => {
  const selectedOptions = req.body.selectedOptions;
  const selectedDimensions = req.body.selectedDimensions;
  const selectedMesures = req.body.selectedMesures;
  const year = req.body.selectedDates;
  const birthday = req.body.birthday;
  console.log(selectedOptions)
  console.log(selectedDimensions)
  console.log(year)
  


  if (
    (selectedOptions.includes("Total_M_R") &&
    selectedOptions.includes("Prevision_annuelle") &&
    selectedDimensions.includes("Soumissionaire") &&
    (year))
  ) {


    let date = "";
    let date_prevu = "";
    if(year){
      date = year+"-01-01";
      date_prevu ="Prevision_annuelle";
    }
    const Soumissionaire = "Soumissionaire";
    const TMD = "Total_M_R";
    
    // Total des crédits alloués par portef
    AuthController.getTotalMontantRembourcePourChaqueSoumissionairePrevu(Soumissionaire, TMD, date,date_prevu)
      .then((Allobjects) => {
        //console.log("objects", objects);

        // Stocker chaque tableau d'objets dans une variable distincte
        const objectsArray1 = Allobjects[0];
        const objectsArray2 = Allobjects[1];

        console.log(objectsArray1)
        console.log(objectsArray2)

        // Envoyer chaque tableau à la vue
        res.render("AdminFrontOffice/DettesPrevu", { objects: objectsArray1, objectsPrevu: objectsArray2, test: "ahmed" });
      })
      .catch((error) => {
        console.error("Error:", error);
      });

  }else if (
    (selectedOptions.includes("Total_M_R") &&
    selectedOptions.includes("Prevision_annuelle") &&
    selectedDimensions.includes("Titre") &&
    (year))
  ) {


    let date = "";
    let date_prevu = "";
    if(year){
      date = year+"-01-01";
      date_prevu ="Prevision_annuelle";
    }
    const Titre = "Titre";
    const TMD = "Total_Solde";
    
    // Total des crédits alloués par portef
    AuthController.getTotalMontantRembourcePourChaqueTitrePrevu(Titre, TMD, date,date_prevu)
      .then((Allobjects) => {
        //console.log("objects", objects);

        // Stocker chaque tableau d'objets dans une variable distincte
        const objectsArray1 = Allobjects[0];
        const objectsArray2 = Allobjects[1];

        console.log(objectsArray1)
        console.log(objectsArray2)

        // Envoyer chaque tableau à la vue
        res.render("AdminFrontOffice/DettesPrevu", { objects: objectsArray1, objectsPrevu: objectsArray2, test: "ahmed" });
      })
      .catch((error) => {
        console.error("Error:", error);
      });

  }
});


  /******************************* Dette Prévu ******************************/


    /******************************* Dashbord ******************************/
route.post("/dashbordFOFF", body, (req, res) => {
  const year = req.body.selectedDates;
  const birthdayString = req.body.birthday;
  const currentYear = new Date(); // Obtient l'année actuelle

  // Convertir la chaîne de caractères en objet Date en réorganisant les parties
  const parts = birthdayString.split("-");
  const reformattedDateString = `${parts[2]}-${parts[1]}-${parts[0]}`; // Convertir en format cccc-bb-aa

  const birthday = new Date(reformattedDateString);

  if (year <= currentYear || birthday <= currentYear) { // état courant ou bien état passé de trésor

    let date = "";
    if (year) {
      date = year + "-01-01";
    } else if (birthdayString) {
      date = birthdayString;
    }
    console.log(year);
    console.log(birthdayString);

    AuthController.getEtatTresorActually(date)
      .then((Allobjects) => {
        //console.log("objects", objects);

        // Stocker chaque tableau d'objets dans une variable distincte
        const objectsArray1 = Allobjects[0];
        const objectsArray2 = Allobjects[1];
        const objectsArray3 = Allobjects[2];
        const objectsArray4 = Allobjects[3];

        console.log(objectsArray1);
        console.log(objectsArray2);
        console.log(objectsArray3);
        console.log(objectsArray4);

        // Envoyer chaque tableau à la vue
        res.render("AdminFrontOffice/dashbord", {
          CreditsActuals: objectsArray1,
          DepensesActuals: objectsArray2,
          RecettesActuels: objectsArray3,
          TresorActuals: objectsArray4,
          test: "omar",
          alertMessage: ""
        });
      })
      .catch((error) => {
        console.error("Error:", error);
      });

  } else if (year > currentYear || birthday > currentYear) {
    // Envoyer chaque tableau à la vue
    res.render("AdminFrontOffice/dashbord", {
      CreditsActuals: '',
          DepensesActuals: '',
          RecettesActuels: '',
          TresorActuals: '',
      test: "omar",
      alertMessage: "Veuillez entrer une date actuelle ou bien une date passée , svp!"
    });
  }
});


  /******************************* Dashbord ******************************/


  /******************************* Tables ********************************/


  route.get("/Action", (req, res, next) => {
    // Exemple d'utilisation de la fonction GetAllUsersFromEntropot avec une promesse
    AuthController.GetAllActions()
      .then((Actions) => {
        // console.log(Actions)
        res.render("AdminFrontOffice/Tables/Action", { Actions: Actions, test: "" });
      })
      .catch((error) => {
        console.error("Erreur lors de la récupération des Actions:", error);
      });
  });



  route.get("/Wilaya", (req, res, next) => {
    // Exemple d'utilisation de la fonction GetAllUsersFromEntropot avec une promesse
    AuthController.GetAllWilayas()
      .then((Wilayas) => {
        console.log(Wilayas)
        res.render("AdminFrontOffice/Tables/Wilayas", { Wilayas: Wilayas, test: "" });
      })
      .catch((error) => {
        console.error("Erreur lors de la récupération des Wilayas:", error);
      });
  });



  route.get("/Dette", (req, res, next) => {
    // Exemple d'utilisation de la fonction GetAllUsersFromEntropot avec une promesse
    AuthController.GetAllDettes()
      .then((Dettes) => {
        console.log(Dettes)
        res.render("AdminFrontOffice/Tables/Dette", { Dettes: Dettes, test: "" });
      })
      .catch((error) => {
        console.error("Erreur lors de la récupération des Mandats :", error);
      });
  });



  route.get("/Credit", (req, res, next) => {
    AuthController.GetAllCredits()
      .then((Credits) => {
        console.log(Credits);
        res.render("AdminFrontOffice/Tables/Crédits", { Credits: Credits, test: "" });
      })
      .catch((error) => {
        console.error("Erreur lors de la récupération des Crédits :", error);
        res.status(500).send("Erreur lors de la récupération des Crédits.");
      });
  });


  route.get("/Recette", (req, res, next) => {
    AuthController.GetAllRecette()
      .then((Recettes) => {
        console.log(Recettes);
        res.render("AdminFrontOffice/Tables/Recette", { Recettes: Recettes, test: "" });
      })
      .catch((error) => {
        console.error("Erreur lors de la récupération des Crédits :", error);
        res.status(500).send("Erreur lors de la récupération des Crédits.");
      });
  });

module.exports = route;
