const route = require("express").Router();
const body = require("express").urlencoded({ urlencoded: true });
const GuardAuth = require("./guardAuth");
const AuthController = require("../Controllers/UserRecettes.Controller");


route.get("/RecettesReelUR", (req, res, next) => {
  res.render("UserRecettes/RecettesReel",{objects:'' , test: "aymene" });
});

route.get("/RecettesPrevuUR", (req, res, next) => {
  res.render("UserRecettes/RecettesPrevu",{objects:'' , objectsPrevu:'', test: "fouzia" });
});











/*************************************************Requetes SQL**********************************************/


/******************************* Recette Réel *******************************/


route.post("/RecettesReelUR", body, (req, res) => {
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
        res.render("UserRecettes/RecettesReel",{ objects: objects, test: "aymene"});
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
        res.render("UserRecettes/RecettesReel",{ objects: objects, test: "aymene"});
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  
  }
});



/******************************* Recette Réel *******************************/





/******************************* Recette Prévu ******************************/
route.post("/RecettesPrevuUR", body, (req, res) => {
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
        res.render("UserRecettes/RecettesPrevu", { objects: objectsArray1, objectsPrevu: objectsArray2, test: "fouzia" });
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
        res.render("UserRecettes/RecettesPrevu", { objects: objectsArray1, objectsPrevu: objectsArray2, test: "fouzia" });
      })
      .catch((error) => {
        console.error("Error:", error);
      });

  }
});


/******************************* Recette Prévu ******************************/













module.exports = route;
