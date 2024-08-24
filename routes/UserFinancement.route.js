const route = require("express").Router();
const body = require("express").urlencoded({ urlencoded: true });
const GuardAuth = require("./guardAuth");
const AuthController = require("../Controllers/UserFinancement.Controller");

route.get("/DettesReelUF", (req, res, next) => {
  res.render("UserFinancement/DettesReelUF", {objects:'', test: "said" });
});

route.get("/DettesPrevuUF", (req, res, next) => {
  res.render("UserFinancement/DettesPrevuUF", {objects:'' , objectsPrevu:'', test: "ahmed" });
});


/********************************* Dette Réel *******************************/

route.post("/DettesReelUF", body, (req, res) => {
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
        res.render("UserFinancement/DettesReelUF",{ objects: objects, test: "said"});
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
        res.render("UserFinancement/DettesReelUF",{ objects: objects, test: "said"});
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  
  } 
});




/********************************* Dette Réel *******************************/


/******************************* Dette Prévu ******************************/
route.post("/DettesPrevuUF", body, (req, res) => {
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
        res.render("UserFinancement/DettesPrevuUF", { objects: objectsArray1, objectsPrevu: objectsArray2, test: "ahmed" });
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
        res.render("UserFinancement/DettesPrevuUF", { objects: objectsArray1, objectsPrevu: objectsArray2, test: "ahmed" });
      })
      .catch((error) => {
        console.error("Error:", error);
      });

  }
});


  /******************************* Dette Prévu ******************************/









module.exports = route;
