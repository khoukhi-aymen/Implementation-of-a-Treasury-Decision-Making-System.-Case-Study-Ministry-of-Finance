/************************************ users ************************************/

const dgram = require("dgram");
const groupSeparator = '\u001D';

// Définition de la fonction pour envoyer les données au serveur Java
function GetAllUsersFromEntropot() {
  return new Promise((resolve, reject) => {
    const client = dgram.createSocket("udp4");
    const serverPort = 4000; // Port sur lequel le serveur Java écoute

    // Créer le message à envoyer
    const message = `All${groupSeparator}Users`;
    const messageBuffer = Buffer.from(message);

    // Écouter la réponse du serveur
    client.on("message", (msg, rinfo) => {
      const msj = msg.toString(); // Message reçu du serveur
      client.close();

      // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau d'utilisateurs
      const usersData = msj.split("\n");

      // Créer un tableau d'objets à partir des données
      const users = usersData.map((userData) => {
        // Diviser les données de l'utilisateur en fonction du séparateur (", ") pour obtenir les attributs
        const userDataArray = userData.split(", ");

        // Créer un objet pour stocker les attributs de l'utilisateur
        const user = {};
        // Parcourir les attributs et les ajouter à l'objet user
        userDataArray.forEach((attr) => {
          // Vérifier si l'attribut contient ':'
          if (attr.includes(":")) {
            // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
            const [key, value] = attr.split(": ");
            // Ajouter l'attribut à l'objet user
            user[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
          } else {
            console.log("Invalid attribute format:", attr);
          }
        });

        return user;
      });
      resolve(users); // Résoudre la promesse avec les utilisateurs récupérés
    });

    // Envoyer le message au serveur Java
    client.send(messageBuffer, serverPort, "localhost", (err) => {
      if (err) {
        console.error("Error while sending data:", err);
        client.close(); // Fermer le socket en cas d'erreur
        reject(err); // Rejeter la promesse en cas d'erreur
      } else {
        console.log("get All Users sent successfully");
      }
    });
  });
}


// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.GetAllUsersFromEntropot = GetAllUsersFromEntropot;



// Définition de la fonction pour envoyer les données au serveur Java
function deleteUserFromEntropot(id) {
  return new Promise((resolve, reject) => {
    const client = dgram.createSocket("udp4");
    const serverPort = 4000; // Port sur lequel le serveur Java écoute

    // Créer le message à envoyer
    const message = `${id}`;
    const messageBuffer = Buffer.from(message);

    // Écouter la réponse du serveur
    client.on("message", (msg, rinfo) => {
      const msj = msg.toString(); // Message reçu du serveur
      client.close();
      console.log(msj)
      resolve(msj); // Résoudre la promesse avec le message récupérés
    });

    // Envoyer le message au serveur Java
    client.send(messageBuffer, serverPort, "localhost", (err) => {
      if (err) {
        console.error("Error while sending data:", err);
        client.close(); // Fermer le socket en cas d'erreur
        reject(err); // Rejeter la promesse en cas d'erreur
      } else {
        console.log("delete User by id sent successfully");
      }
    });
  });
}


// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.deleteUserFromEntropot = deleteUserFromEntropot;



// Définition de la fonction pour envoyer les données au serveur Java
function updateUSER_AU_user_haut_niveau(id,user_haut_niveau) {
  return new Promise((resolve, reject) => {
    const client = dgram.createSocket("udp4");
    const serverPort = 4000; // Port sur lequel le serveur Java écoute

    // Créer le message à envoyer
    const message = `${id}${groupSeparator}${user_haut_niveau}${groupSeparator}moi${groupSeparator}moi${groupSeparator}moi`;
    const messageBuffer = Buffer.from(message);

    // Écouter la réponse du serveur
    client.on("message", (msg, rinfo) => {
      const msj = msg.toString(); // Message reçu du serveur
      client.close();
      console.log(msj)
      resolve(msj); // Résoudre la promesse avec le message récupérés
    });

    // Envoyer le message au serveur Java
    client.send(messageBuffer, serverPort, "localhost", (err) => {
      if (err) {
        console.error("Error while sending data:", err);
        client.close(); // Fermer le socket en cas d'erreur
        reject(err); // Rejeter la promesse en cas d'erreur
      } else {
        console.log("update User to user_haut_niveau sent successfully");
      }
    });
  });
}


// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.updateUSER_AU_user_haut_niveau = updateUSER_AU_user_haut_niveau;


// Définition de la fonction pour envoyer les données au serveur Java
function updateUSER_AU_user_Dépenses(id,user_depenses) {
  return new Promise((resolve, reject) => {
    const client = dgram.createSocket("udp4");
    const serverPort = 4000; // Port sur lequel le serveur Java écoute

    // Créer le message à envoyer
    const message = `${id}${groupSeparator}${user_depenses}${groupSeparator}moi${groupSeparator}moi${groupSeparator}lui`;
    const messageBuffer = Buffer.from(message);

    // Écouter la réponse du serveur
    client.on("message", (msg, rinfo) => {
      const msj = msg.toString(); // Message reçu du serveur
      client.close();
      console.log(msj)
      resolve(msj); // Résoudre la promesse avec le message récupérés
    });

    // Envoyer le message au serveur Java
    client.send(messageBuffer, serverPort, "localhost", (err) => {
      if (err) {
        console.error("Error while sending data:", err);
        client.close(); // Fermer le socket en cas d'erreur
        reject(err); // Rejeter la promesse en cas d'erreur
      } else {
        console.log("update User to user_dépenses sent successfully");
      }
    });
  });
}


// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.updateUSER_AU_user_Dépenses = updateUSER_AU_user_Dépenses;



// Définition de la fonction pour envoyer les données au serveur Java
function updateUSER_AU_Admin_Back_Office(id,AdminBackOffice) {
  return new Promise((resolve, reject) => {
    const client = dgram.createSocket("udp4");
    const serverPort = 4000; // Port sur lequel le serveur Java écoute

    // Créer le message à envoyer
    const message = `${id}${groupSeparator}${AdminBackOffice}${groupSeparator}moi${groupSeparator}moi${groupSeparator}leur`;
    const messageBuffer = Buffer.from(message);

    // Écouter la réponse du serveur
    client.on("message", (msg, rinfo) => {
      const msj = msg.toString(); // Message reçu du serveur
      client.close();
      console.log(msj)
      resolve(msj); // Résoudre la promesse avec le message récupérés
    });

    // Envoyer le message au serveur Java
    client.send(messageBuffer, serverPort, "localhost", (err) => {
      if (err) {
        console.error("Error while sending data:", err);
        client.close(); // Fermer le socket en cas d'erreur
        reject(err); // Rejeter la promesse en cas d'erreur
      } else {
        console.log("update User to Admin back-office sent successfully");
      }
    });
  });
}


// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.updateUSER_AU_Admin_Back_Office = updateUSER_AU_Admin_Back_Office;



// Définition de la fonction pour envoyer les données au serveur Java
function updateUSER_AU_Admin_Front_Office(id,AdminFrontOffice) {
  return new Promise((resolve, reject) => {
    const client = dgram.createSocket("udp4");
    const serverPort = 4000; // Port sur lequel le serveur Java écoute

    // Créer le message à envoyer
    const message = `${id}${groupSeparator}${AdminFrontOffice}${groupSeparator}moi${groupSeparator}moi${groupSeparator}nous`;
    const messageBuffer = Buffer.from(message);

    // Écouter la réponse du serveur
    client.on("message", (msg, rinfo) => {
      const msj = msg.toString(); // Message reçu du serveur
      client.close();
      console.log(msj)
      resolve(msj); // Résoudre la promesse avec le message récupérés
    });

    // Envoyer le message au serveur Java
    client.send(messageBuffer, serverPort, "localhost", (err) => {
      if (err) {
        console.error("Error while sending data:", err);
        client.close(); // Fermer le socket en cas d'erreur
        reject(err); // Rejeter la promesse en cas d'erreur
      } else {
        console.log("update User to Admin front-office sent successfully");
      }
    });
  });
}


// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.updateUSER_AU_Admin_Front_Office = updateUSER_AU_Admin_Front_Office;



// Définition de la fonction pour envoyer les données au serveur Java
function updateUSER_AU_User_Financement(id,UserFinancement) {
  return new Promise((resolve, reject) => {
    const client = dgram.createSocket("udp4");
    const serverPort = 4000; // Port sur lequel le serveur Java écoute

    // Créer le message à envoyer
    const message = `${id}${groupSeparator}${UserFinancement}${groupSeparator}moi${groupSeparator}moi${groupSeparator}vous`;
    const messageBuffer = Buffer.from(message);

    // Écouter la réponse du serveur
    client.on("message", (msg, rinfo) => {
      const msj = msg.toString(); // Message reçu du serveur
      client.close();
      console.log(msj)
      resolve(msj); // Résoudre la promesse avec le message récupérés
    });

    // Envoyer le message au serveur Java
    client.send(messageBuffer, serverPort, "localhost", (err) => {
      if (err) {
        console.error("Error while sending data:", err);
        client.close(); // Fermer le socket en cas d'erreur
        reject(err); // Rejeter la promesse en cas d'erreur
      } else {
        console.log("update User to User Financement sent successfully");
      }
    });
  });
}


// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.updateUSER_AU_User_Financement = updateUSER_AU_User_Financement;





// Définition de la fonction pour envoyer les données au serveur Java
function updateUSER_AU_User_Recettes(id,UserRecettes) {
  return new Promise((resolve, reject) => {
    const client = dgram.createSocket("udp4");
    const serverPort = 4000; // Port sur lequel le serveur Java écoute

    // Créer le message à envoyer
    const message = `${id}${groupSeparator}${UserRecettes}${groupSeparator}moi${groupSeparator}moi${groupSeparator}tu`;
    const messageBuffer = Buffer.from(message);

    // Écouter la réponse du serveur
    client.on("message", (msg, rinfo) => {
      const msj = msg.toString(); // Message reçu du serveur
      client.close();
      console.log(msj)
      resolve(msj); // Résoudre la promesse avec le message récupérés
    });

    // Envoyer le message au serveur Java
    client.send(messageBuffer, serverPort, "localhost", (err) => {
      if (err) {
        console.error("Error while sending data:", err);
        client.close(); // Fermer le socket en cas d'erreur
        reject(err); // Rejeter la promesse en cas d'erreur
      } else {
        console.log("update User to User Recettes sent successfully");
      }
    });
  });
}


// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.updateUSER_AU_User_Recettes = updateUSER_AU_User_Recettes;

/******************************** users ***************************/


/************************************************ Requetes SQL ****************************************************/

                     /***************************** Crédits réel ***************************/


// Définition de la fonction pour envoyer les données au serveur Java
function getTotalCreditsAllouePourChaquePortefeuilleReel(Portefeuille,TMD,date) {
    return new Promise((resolve, reject) => {
      const client = dgram.createSocket("udp4");
      const serverPort = 4000; // Port sur lequel le serveur Java écoute

      // Créer le message à envoyer
      const message = `${Portefeuille}${groupSeparator}${TMD}${groupSeparator}${date}${groupSeparator}Credits${groupSeparator}reel${groupSeparator}a${groupSeparator}SSSSSSS`;
      const messageBuffer = Buffer.from(message);
  
      // Écouter la réponse du serveur
    client.on("message", (msg, rinfo) => {
      const msj = msg.toString(); // Message reçu du serveur
      client.close();

      // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau 
      const Datas = msj.split("\n");

      // Créer un tableau d'objets à partir des données
      const objects = Datas.map((Data) => {
        // Diviser les données  en fonction du séparateur (", ") pour obtenir les attributs
        const DataArray = Data.split(", ");

        // Créer un objet pour stocker les attributs 
        const object = {};
        // Parcourir les attributs et les ajouter à l'objet 
        DataArray.forEach((attr) => {
          // Vérifier si l'attribut contient ':'
          if (attr.includes(":")) {
            // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
            const [key, value] = attr.split(": ");
            // Ajouter l'attribut à l'objet 
            object[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
          } else {
            console.log("Invalid attribute format:", attr);
          }
        });

        return object;
      });
      resolve(objects); // Résoudre la promesse avec les utilisateurs récupérés
    });
  
      // Envoyer le message au serveur Java
      client.send(messageBuffer, serverPort, "localhost", (err) => {
        if (err) {
          console.error("Error while sending data:", err);
          client.close(); // Fermer le socket en cas d'erreur
          reject(err); // Rejeter la promesse en cas d'erreur
        } else {
            console.log(`Données ${Portefeuille} et ${TMD} et ${date} envoyées avec succès !`);
        }
      });
    });
  }
  
  
  // Exporter la fonction pour l'utiliser dans d'autres modules
  module.exports.getTotalCreditsAllouePourChaquePortefeuilleReel = getTotalCreditsAllouePourChaquePortefeuilleReel;





  // Définition de la fonction pour envoyer les données au serveur Java
function getTotalCreditsAllouePourChaqueProgrammeReel(Programme,TMD,date) {
  return new Promise((resolve, reject) => {
    const client = dgram.createSocket("udp4");
    const serverPort = 4000; // Port sur lequel le serveur Java écoute

    // Créer le message à envoyer
    const message = `${Programme}${groupSeparator}${TMD}${groupSeparator}${date}${groupSeparator}Credits${groupSeparator}reel${groupSeparator}b${groupSeparator}SSSSSSS`;
    const messageBuffer = Buffer.from(message);

    // Écouter la réponse du serveur
  client.on("message", (msg, rinfo) => {
    const msj = msg.toString(); // Message reçu du serveur
    client.close();

    // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau 
    const Datas = msj.split("\n");

    // Créer un tableau d'objets à partir des données
    const objects = Datas.map((Data) => {
      // Diviser les données  en fonction du séparateur (", ") pour obtenir les attributs
      const DataArray = Data.split(", ");

      // Créer un objet pour stocker les attributs 
      const object = {};
      // Parcourir les attributs et les ajouter à l'objet 
      DataArray.forEach((attr) => {
        // Vérifier si l'attribut contient ':'
        if (attr.includes(":")) {
          // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
          const [key, value] = attr.split(": ");
          // Ajouter l'attribut à l'objet 
          object[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
        } else {
          console.log("Invalid attribute format:", attr);
        }
      });

      return object;
    });
    resolve(objects); // Résoudre la promesse avec les utilisateurs récupérés
  });

    // Envoyer le message au serveur Java
    client.send(messageBuffer, serverPort, "localhost", (err) => {
      if (err) {
        console.error("Error while sending data:", err);
        client.close(); // Fermer le socket en cas d'erreur
        reject(err); // Rejeter la promesse en cas d'erreur
      } else {
          console.log(`Données ${Programme} et ${TMD} et ${date} envoyées avec succès !`);
      }
    });
  });
}


// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.getTotalCreditsAllouePourChaqueProgrammeReel = getTotalCreditsAllouePourChaqueProgrammeReel;




  // Définition de la fonction pour envoyer les données au serveur Java
  function getTotalCreditsAllouePourChaqueSousProgrammeReel(SousProgramme,TMD,date) {
    return new Promise((resolve, reject) => {
      const client = dgram.createSocket("udp4");
      const serverPort = 4000; // Port sur lequel le serveur Java écoute
  
      // Créer le message à envoyer
      const message = `${SousProgramme}${groupSeparator}${TMD}${groupSeparator}${date}${groupSeparator}Credits${groupSeparator}reel${groupSeparator}c${groupSeparator}SSSSSSS`;
      const messageBuffer = Buffer.from(message);
  
      // Écouter la réponse du serveur
    client.on("message", (msg, rinfo) => {
      const msj = msg.toString(); // Message reçu du serveur
      client.close();
  
      // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau 
      const Datas = msj.split("\n");
  
      // Créer un tableau d'objets à partir des données
      const objects = Datas.map((Data) => {
        // Diviser les données  en fonction du séparateur (", ") pour obtenir les attributs
        const DataArray = Data.split(", ");
  
        // Créer un objet pour stocker les attributs 
        const object = {};
        // Parcourir les attributs et les ajouter à l'objet 
        DataArray.forEach((attr) => {
          // Vérifier si l'attribut contient ':'
          if (attr.includes(":")) {
            // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
            const [key, value] = attr.split(": ");
            // Ajouter l'attribut à l'objet 
            object[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
          } else {
            console.log("Invalid attribute format:", attr);
          }
        });
  
        return object;
      });
      resolve(objects); // Résoudre la promesse avec les utilisateurs récupérés
    });
  
      // Envoyer le message au serveur Java
      client.send(messageBuffer, serverPort, "localhost", (err) => {
        if (err) {
          console.error("Error while sending data:", err);
          client.close(); // Fermer le socket en cas d'erreur
          reject(err); // Rejeter la promesse en cas d'erreur
        } else {
            console.log(`Données ${SousProgramme} et ${TMD} et ${date} envoyées avec succès !`);
        }
      });
    });
  }
  
  
  // Exporter la fonction pour l'utiliser dans d'autres modules
  module.exports.getTotalCreditsAllouePourChaqueSousProgrammeReel = getTotalCreditsAllouePourChaqueSousProgrammeReel;




  // Définition de la fonction pour envoyer les données au serveur Java
  function getTotalCreditsAllouePourChaqueTitreReel(Titre,TMD,date) {
    return new Promise((resolve, reject) => {
      const client = dgram.createSocket("udp4");
      const serverPort = 4000; // Port sur lequel le serveur Java écoute
  
      // Créer le message à envoyer
      const message = `${Titre}${groupSeparator}${TMD}${groupSeparator}${date}${groupSeparator}Credits${groupSeparator}reel${groupSeparator}d${groupSeparator}SSSSSSS`;
      const messageBuffer = Buffer.from(message);
  
      // Écouter la réponse du serveur
    client.on("message", (msg, rinfo) => {
      const msj = msg.toString(); // Message reçu du serveur
      client.close();
  
      // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau 
      const Datas = msj.split("\n");
  
      // Créer un tableau d'objets à partir des données
      const objects = Datas.map((Data) => {
        // Diviser les données  en fonction du séparateur (", ") pour obtenir les attributs
        const DataArray = Data.split(", ");
  
        // Créer un objet pour stocker les attributs 
        const object = {};
        // Parcourir les attributs et les ajouter à l'objet 
        DataArray.forEach((attr) => {
          // Vérifier si l'attribut contient ':'
          if (attr.includes(":")) {
            // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
            const [key, value] = attr.split(": ");
            // Ajouter l'attribut à l'objet 
            object[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
          } else {
            console.log("Invalid attribute format:", attr);
          }
        });
  
        return object;
      });
      resolve(objects); // Résoudre la promesse avec les utilisateurs récupérés
    });
  
      // Envoyer le message au serveur Java
      client.send(messageBuffer, serverPort, "localhost", (err) => {
        if (err) {
          console.error("Error while sending data:", err);
          client.close(); // Fermer le socket en cas d'erreur
          reject(err); // Rejeter la promesse en cas d'erreur
        } else {
            console.log(`Données ${Titre} et ${TMD} et ${date} envoyées avec succès !`);
        }
      });
    });
  }
  
  
  // Exporter la fonction pour l'utiliser dans d'autres modules
  module.exports.getTotalCreditsAllouePourChaqueTitreReel = getTotalCreditsAllouePourChaqueTitreReel;

  


  // Définition de la fonction pour envoyer les données au serveur Java
  function getTotalCreditsAllouePourChaqueCategorieReel(Categorie,TMD,date) {
    return new Promise((resolve, reject) => {
      const client = dgram.createSocket("udp4");
      const serverPort = 4000; // Port sur lequel le serveur Java écoute
  
      // Créer le message à envoyer
      const message = `${Categorie}${groupSeparator}${TMD}${groupSeparator}${date}${groupSeparator}Credits${groupSeparator}reel${groupSeparator}e${groupSeparator}SSSSSSS`;
      const messageBuffer = Buffer.from(message);
  
      // Écouter la réponse du serveur
    client.on("message", (msg, rinfo) => {
      const msj = msg.toString(); // Message reçu du serveur
      client.close();
  
      // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau 
      const Datas = msj.split("\n");
  
      // Créer un tableau d'objets à partir des données
      const objects = Datas.map((Data) => {
        // Diviser les données  en fonction du séparateur (", ") pour obtenir les attributs
        const DataArray = Data.split(", ");
  
        // Créer un objet pour stocker les attributs 
        const object = {};
        // Parcourir les attributs et les ajouter à l'objet 
        DataArray.forEach((attr) => {
          // Vérifier si l'attribut contient ':'
          if (attr.includes(":")) {
            // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
            const [key, value] = attr.split(": ");
            // Ajouter l'attribut à l'objet 
            object[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
          } else {
            console.log("Invalid attribute format:", attr);
          }
        });
  
        return object;
      });
      resolve(objects); // Résoudre la promesse avec les utilisateurs récupérés
    });
  
      // Envoyer le message au serveur Java
      client.send(messageBuffer, serverPort, "localhost", (err) => {
        if (err) {
          console.error("Error while sending data:", err);
          client.close(); // Fermer le socket en cas d'erreur
          reject(err); // Rejeter la promesse en cas d'erreur
        } else {
            console.log(`Données ${Categorie} et ${TMD} et ${date} envoyées avec succès !`);
        }
      });
    });
  }
  
  
  // Exporter la fonction pour l'utiliser dans d'autres modules
  module.exports.getTotalCreditsAllouePourChaqueCategorieReel = getTotalCreditsAllouePourChaqueCategorieReel;



                       /***************************** Crédits réel ***************************/


                       /***************************** Crédits prévu ***************************/


// Définition de la fonction pour envoyer les données au serveur Java

function getTotalCreditsAllouePourChaquePortefeuillePrevu(Portefeuille, TMD, date,date_prevu) {
  return new Promise((resolve, reject) => {
      const client = dgram.createSocket("udp4");
      let receivedObjects = [];
      const TOTAL_EXPECTED_OBJECTS = 2;
      const serverPort = 4000; // Port sur lequel le serveur Java écoute

      // Écouter la réponse du serveur
      client.on("message", (msg, rinfo) => {
    
          const msj = msg.toString(); // Message reçu du serveur

          // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau
          const Datas = msj.split("\n");

          // Créer un tableau d'objets à partir des données
          const objects = Datas.map((Data) => {
              // Diviser les données  en fonction du séparateur (", ") pour obtenir les attributs
              const DataArray = Data.split(", ");

              // Créer un objet pour stocker les attributs
              const object = {};
              // Parcourir les attributs et les ajouter à l'objet
              DataArray.forEach((attr) => {
                  // Vérifier si l'attribut contient ':'
                  if (attr.includes(":")) {
                     
                      // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
                      const [key, value] = attr.split(": ");
                      // Ajouter l'attribut à l'objet
                      object[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
                  } else {
                      console.log("Invalid attribute format:", attr);
                  }
              });

              return object;
          });

          // Ajouter le tableau d'objets actuel à la liste des tableaux reçus
          receivedObjects.push(objects);

          // Vérifier si tous les tableaux d'objets attendus ont été reçus
          if (receivedObjects.length === TOTAL_EXPECTED_OBJECTS) {
              // Résoudre la promesse avec tous les tableaux d'objets reçus
              resolve(receivedObjects);
          }
      });

      // Créer le message à envoyer
      const message = `${Portefeuille}${groupSeparator}${TMD}${groupSeparator}${date}${groupSeparator}Credits${groupSeparator}prevu${groupSeparator}a${groupSeparator}${date_prevu}`;
      const messageBuffer = Buffer.from(message);

      // Envoyer le message au serveur Java
      client.send(messageBuffer, serverPort, "localhost", (err) => {
          if (err) {
              console.error("Error while sending data:", err);
              client.close(); // Fermer le socket en cas d'erreur
              reject(err); // Rejeter la promesse en cas d'erreur
          } else {
              console.log(`Données ${Portefeuille} et ${TMD} et ${date} et ${date_prevu} envoyées avec succès !`);
          }
      });
  });
}



// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.getTotalCreditsAllouePourChaquePortefeuillePrevu = getTotalCreditsAllouePourChaquePortefeuillePrevu;



// Définition de la fonction pour envoyer les données au serveur Java

function getTotalCreditsAllouePourChaqueProgrammePrevu(Programme, TMD, date,date_prevu) {
  return new Promise((resolve, reject) => {
      const client = dgram.createSocket("udp4");
      let receivedObjects = [];
      const TOTAL_EXPECTED_OBJECTS = 2;
      const serverPort = 4000; // Port sur lequel le serveur Java écoute

      // Écouter la réponse du serveur
      client.on("message", (msg, rinfo) => {
    
          const msj = msg.toString(); // Message reçu du serveur

          // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau
          const Datas = msj.split("\n");

          // Créer un tableau d'objets à partir des données
          const objects = Datas.map((Data) => {
              // Diviser les données  en fonction du séparateur (", ") pour obtenir les attributs
              const DataArray = Data.split(", ");

              // Créer un objet pour stocker les attributs
              const object = {};
              // Parcourir les attributs et les ajouter à l'objet
              DataArray.forEach((attr) => {
                  // Vérifier si l'attribut contient ':'
                  if (attr.includes(":")) {
                     
                      // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
                      const [key, value] = attr.split(": ");
                      // Ajouter l'attribut à l'objet
                      object[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
                  } else {
                      console.log("Invalid attribute format:", attr);
                  }
              });

              return object;
          });

          // Ajouter le tableau d'objets actuel à la liste des tableaux reçus
          receivedObjects.push(objects);

          // Vérifier si tous les tableaux d'objets attendus ont été reçus
          if (receivedObjects.length === TOTAL_EXPECTED_OBJECTS) {
              // Résoudre la promesse avec tous les tableaux d'objets reçus
              resolve(receivedObjects);
          }
      });

      // Créer le message à envoyer
      const message = `${Programme}${groupSeparator}${TMD}${groupSeparator}${date}${groupSeparator}Credits${groupSeparator}prevu${groupSeparator}b${groupSeparator}${date_prevu}`;
      const messageBuffer = Buffer.from(message);

      // Envoyer le message au serveur Java
      client.send(messageBuffer, serverPort, "localhost", (err) => {
          if (err) {
              console.error("Error while sending data:", err);
              client.close(); // Fermer le socket en cas d'erreur
              reject(err); // Rejeter la promesse en cas d'erreur
          } else {
              console.log(`Données ${Programme} et ${TMD} et ${date} et ${date_prevu} envoyées avec succès !`);
          }
      });
  });
}



// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.getTotalCreditsAllouePourChaqueProgrammePrevu = getTotalCreditsAllouePourChaqueProgrammePrevu;



// Définition de la fonction pour envoyer les données au serveur Java

function getTotalCreditsAllouePourChaqueSousProgrammePrevu(SousProgramme, TMD, date,date_prevu) {
  return new Promise((resolve, reject) => {
      const client = dgram.createSocket("udp4");
      let receivedObjects = [];
      const TOTAL_EXPECTED_OBJECTS = 2;
      const serverPort = 4000; // Port sur lequel le serveur Java écoute

      // Écouter la réponse du serveur
      client.on("message", (msg, rinfo) => {
    
          const msj = msg.toString(); // Message reçu du serveur

          // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau
          const Datas = msj.split("\n");

          // Créer un tableau d'objets à partir des données
          const objects = Datas.map((Data) => {
              // Diviser les données  en fonction du séparateur (", ") pour obtenir les attributs
              const DataArray = Data.split(", ");

              // Créer un objet pour stocker les attributs
              const object = {};
              // Parcourir les attributs et les ajouter à l'objet
              DataArray.forEach((attr) => {
                  // Vérifier si l'attribut contient ':'
                  if (attr.includes(":")) {
                     
                      // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
                      const [key, value] = attr.split(": ");
                      // Ajouter l'attribut à l'objet
                      object[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
                  } else {
                      console.log("Invalid attribute format:", attr);
                  }
              });

              return object;
          });

          // Ajouter le tableau d'objets actuel à la liste des tableaux reçus
          receivedObjects.push(objects);

          // Vérifier si tous les tableaux d'objets attendus ont été reçus
          if (receivedObjects.length === TOTAL_EXPECTED_OBJECTS) {
              // Résoudre la promesse avec tous les tableaux d'objets reçus
              resolve(receivedObjects);
          }
      });

      // Créer le message à envoyer
      const message = `${SousProgramme}${groupSeparator}${TMD}${groupSeparator}${date}${groupSeparator}Credits${groupSeparator}prevu${groupSeparator}c${groupSeparator}${date_prevu}`;
      const messageBuffer = Buffer.from(message);

      // Envoyer le message au serveur Java
      client.send(messageBuffer, serverPort, "localhost", (err) => {
          if (err) {
              console.error("Error while sending data:", err);
              client.close(); // Fermer le socket en cas d'erreur
              reject(err); // Rejeter la promesse en cas d'erreur
          } else {
              console.log(`Données ${SousProgramme} et ${TMD} et ${date} et ${date_prevu} envoyées avec succès !`);
          }
      });
  });
}



// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.getTotalCreditsAllouePourChaqueSousProgrammePrevu = getTotalCreditsAllouePourChaqueSousProgrammePrevu;




// Définition de la fonction pour envoyer les données au serveur Java

function getTotalCreditsAllouePourChaqueTitrePrevu(Titre, TMD, date,date_prevu) {
  return new Promise((resolve, reject) => {
      const client = dgram.createSocket("udp4");
      let receivedObjects = [];
      const TOTAL_EXPECTED_OBJECTS = 2;
      const serverPort = 4000; // Port sur lequel le serveur Java écoute

      // Écouter la réponse du serveur
      client.on("message", (msg, rinfo) => {
    
          const msj = msg.toString(); // Message reçu du serveur

          // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau
          const Datas = msj.split("\n");

          // Créer un tableau d'objets à partir des données
          const objects = Datas.map((Data) => {
              // Diviser les données  en fonction du séparateur (", ") pour obtenir les attributs
              const DataArray = Data.split(", ");

              // Créer un objet pour stocker les attributs
              const object = {};
              // Parcourir les attributs et les ajouter à l'objet
              DataArray.forEach((attr) => {
                  // Vérifier si l'attribut contient ':'
                  if (attr.includes(":")) {
                     
                      // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
                      const [key, value] = attr.split(": ");
                      // Ajouter l'attribut à l'objet
                      object[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
                  } else {
                      console.log("Invalid attribute format:", attr);
                  }
              });

              return object;
          });

          // Ajouter le tableau d'objets actuel à la liste des tableaux reçus
          receivedObjects.push(objects);

          // Vérifier si tous les tableaux d'objets attendus ont été reçus
          if (receivedObjects.length === TOTAL_EXPECTED_OBJECTS) {
              // Résoudre la promesse avec tous les tableaux d'objets reçus
              resolve(receivedObjects);
          }
      });

      // Créer le message à envoyer
      const message = `${Titre}${groupSeparator}${TMD}${groupSeparator}${date}${groupSeparator}Credits${groupSeparator}prevu${groupSeparator}d${groupSeparator}${date_prevu}`;
      const messageBuffer = Buffer.from(message);

      // Envoyer le message au serveur Java
      client.send(messageBuffer, serverPort, "localhost", (err) => {
          if (err) {
              console.error("Error while sending data:", err);
              client.close(); // Fermer le socket en cas d'erreur
              reject(err); // Rejeter la promesse en cas d'erreur
          } else {
              console.log(`Données ${Titre} et ${TMD} et ${date} et ${date_prevu} envoyées avec succès !`);
          }
      });
  });
}



// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.getTotalCreditsAllouePourChaqueTitrePrevu = getTotalCreditsAllouePourChaqueTitrePrevu;

getTotalCreditsAllouePourChaqueTitrePrevu

// Définition de la fonction pour envoyer les données au serveur Java

function getTotalCreditsAllouePourChaqueCategoriePrevu(Categorie, TMD, date,date_prevu) {
  return new Promise((resolve, reject) => {
      const client = dgram.createSocket("udp4");
      let receivedObjects = [];
      const TOTAL_EXPECTED_OBJECTS = 2;
      const serverPort = 4000; // Port sur lequel le serveur Java écoute

      // Écouter la réponse du serveur
      client.on("message", (msg, rinfo) => {
    
          const msj = msg.toString(); // Message reçu du serveur

          // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau
          const Datas = msj.split("\n");

          // Créer un tableau d'objets à partir des données
          const objects = Datas.map((Data) => {
              // Diviser les données  en fonction du séparateur (", ") pour obtenir les attributs
              const DataArray = Data.split(", ");

              // Créer un objet pour stocker les attributs
              const object = {};
              // Parcourir les attributs et les ajouter à l'objet
              DataArray.forEach((attr) => {
                  // Vérifier si l'attribut contient ':'
                  if (attr.includes(":")) {
                     
                      // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
                      const [key, value] = attr.split(": ");
                      // Ajouter l'attribut à l'objet
                      object[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
                  } else {
                      console.log("Invalid attribute format:", attr);
                  }
              });

              return object;
          });

          // Ajouter le tableau d'objets actuel à la liste des tableaux reçus
          receivedObjects.push(objects);

          // Vérifier si tous les tableaux d'objets attendus ont été reçus
          if (receivedObjects.length === TOTAL_EXPECTED_OBJECTS) {
              // Résoudre la promesse avec tous les tableaux d'objets reçus
              resolve(receivedObjects);
          }
      });

      // Créer le message à envoyer
      const message = `${Categorie}${groupSeparator}${TMD}${groupSeparator}${date}${groupSeparator}Credits${groupSeparator}prevu${groupSeparator}e${groupSeparator}${date_prevu}`;
      const messageBuffer = Buffer.from(message);

      // Envoyer le message au serveur Java
      client.send(messageBuffer, serverPort, "localhost", (err) => {
          if (err) {
              console.error("Error while sending data:", err);
              client.close(); // Fermer le socket en cas d'erreur
              reject(err); // Rejeter la promesse en cas d'erreur
          } else {
              console.log(`Données ${Categorie} et ${TMD} et ${date} et ${date_prevu} envoyées avec succès !`);
          }
      });
  });
}



// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.getTotalCreditsAllouePourChaqueCategoriePrevu = getTotalCreditsAllouePourChaqueCategoriePrevu;

             /***************************** Crédits prévu ***************************/


             /************************************* Dépenses réel ************************************/


// Définition de la fonction pour envoyer les données au serveur Java
function getTotalMontantdepensePourChaquePortefeuilleReel(Portefeuille,TMD,date) {
  return new Promise((resolve, reject) => {
    const client = dgram.createSocket("udp4");
    const serverPort = 4000; // Port sur lequel le serveur Java écoute

    // Créer le message à envoyer
    const message = `${Portefeuille}${groupSeparator}${TMD}${groupSeparator}${date}${groupSeparator}Depenses${groupSeparator}reel${groupSeparator}a${groupSeparator}SSSSSSS`;
    const messageBuffer = Buffer.from(message);

    // Écouter la réponse du serveur
  client.on("message", (msg, rinfo) => {
    const msj = msg.toString(); // Message reçu du serveur
    client.close();

    // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau 
    const Datas = msj.split("\n");

    // Créer un tableau d'objets à partir des données
    const objects = Datas.map((Data) => {
      // Diviser les données  en fonction du séparateur (", ") pour obtenir les attributs
      const DataArray = Data.split(", ");

      // Créer un objet pour stocker les attributs 
      const object = {};
      // Parcourir les attributs et les ajouter à l'objet 
      DataArray.forEach((attr) => {
        // Vérifier si l'attribut contient ':'
        if (attr.includes(":")) {
          // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
          const [key, value] = attr.split(": ");
          // Ajouter l'attribut à l'objet 
          object[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
        } else {
          console.log("Invalid attribute format:", attr);
        }
      });

      return object;
    });
    resolve(objects); // Résoudre la promesse avec les utilisateurs récupérés
  });

    // Envoyer le message au serveur Java
    client.send(messageBuffer, serverPort, "localhost", (err) => {
      if (err) {
        console.error("Error while sending data:", err);
        client.close(); // Fermer le socket en cas d'erreur
        reject(err); // Rejeter la promesse en cas d'erreur
      } else {
          console.log(`Données ${Portefeuille} et ${TMD} et ${date} envoyées avec succès !`);
      }
    });
  });
}


// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.getTotalMontantdepensePourChaquePortefeuilleReel = getTotalMontantdepensePourChaquePortefeuilleReel;





// Définition de la fonction pour envoyer les données au serveur Java
function getTotalMontantdepensePourChaqueProgrammeReel(Programme,TMD, date) {
return new Promise((resolve, reject) => {
  const client = dgram.createSocket("udp4");
  const serverPort = 4000; // Port sur lequel le serveur Java écoute

  // Créer le message à envoyer
  const message = `${Programme}${groupSeparator}${TMD}${groupSeparator}${date}${groupSeparator}Depenses${groupSeparator}reel${groupSeparator}b${groupSeparator}SSSSSSS`;
  const messageBuffer = Buffer.from(message);

  // Écouter la réponse du serveur
client.on("message", (msg, rinfo) => {
  const msj = msg.toString(); // Message reçu du serveur
  client.close();

  // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau 
  const Datas = msj.split("\n");

  // Créer un tableau d'objets à partir des données
  const objects = Datas.map((Data) => {
    // Diviser les données  en fonction du séparateur (", ") pour obtenir les attributs
    const DataArray = Data.split(", ");

    // Créer un objet pour stocker les attributs 
    const object = {};
    // Parcourir les attributs et les ajouter à l'objet 
    DataArray.forEach((attr) => {
      // Vérifier si l'attribut contient ':'
      if (attr.includes(":")) {
        // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
        const [key, value] = attr.split(": ");
        // Ajouter l'attribut à l'objet 
        object[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
      } else {
        console.log("Invalid attribute format:", attr);
      }
    });

    return object;
  });
  resolve(objects); // Résoudre la promesse avec les utilisateurs récupérés
});

  // Envoyer le message au serveur Java
  client.send(messageBuffer, serverPort, "localhost", (err) => {
    if (err) {
      console.error("Error while sending data:", err);
      client.close(); // Fermer le socket en cas d'erreur
      reject(err); // Rejeter la promesse en cas d'erreur
    } else {
        console.log(`Données ${Programme} et ${TMD} et ${date} envoyées avec succès !`);
    }
  });
});
}


// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.getTotalMontantdepensePourChaqueProgrammeReel = getTotalMontantdepensePourChaqueProgrammeReel;




// Définition de la fonction pour envoyer les données au serveur Java
function getTotalMontantdepensePourChaqueSousProgrammeReel(SousProgramme,TMD,date) {
  return new Promise((resolve, reject) => {
    const client = dgram.createSocket("udp4");
    const serverPort = 4000; // Port sur lequel le serveur Java écoute

    // Créer le message à envoyer
    const message = `${SousProgramme}${groupSeparator}${TMD}${groupSeparator}${date}${groupSeparator}Depenses${groupSeparator}reel${groupSeparator}c${groupSeparator}SSSSSSS`;
    const messageBuffer = Buffer.from(message);

    // Écouter la réponse du serveur
  client.on("message", (msg, rinfo) => {
    const msj = msg.toString(); // Message reçu du serveur
    client.close();

    // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau 
    const Datas = msj.split("\n");

    // Créer un tableau d'objets à partir des données
    const objects = Datas.map((Data) => {
      // Diviser les données  en fonction du séparateur (", ") pour obtenir les attributs
      const DataArray = Data.split(", ");

      // Créer un objet pour stocker les attributs 
      const object = {};
      // Parcourir les attributs et les ajouter à l'objet 
      DataArray.forEach((attr) => {
        // Vérifier si l'attribut contient ':'
        if (attr.includes(":")) {
          // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
          const [key, value] = attr.split(": ");
          // Ajouter l'attribut à l'objet 
          object[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
        } else {
          console.log("Invalid attribute format:", attr);
        }
      });

      return object;
    });
    resolve(objects); // Résoudre la promesse avec les utilisateurs récupérés
  });

    // Envoyer le message au serveur Java
    client.send(messageBuffer, serverPort, "localhost", (err) => {
      if (err) {
        console.error("Error while sending data:", err);
        client.close(); // Fermer le socket en cas d'erreur
        reject(err); // Rejeter la promesse en cas d'erreur
      } else {
          console.log(`Données ${SousProgramme} et ${TMD} et ${date} envoyées avec succès !`);
      }
    });
  });
}


// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.getTotalMontantdepensePourChaqueSousProgrammeReel = getTotalMontantdepensePourChaqueSousProgrammeReel;




// Définition de la fonction pour envoyer les données au serveur Java
function getTotalMontantdepensePourChaqueTitreReel(Titre,TMD,date) {
  return new Promise((resolve, reject) => {
    const client = dgram.createSocket("udp4");
    const serverPort = 4000; // Port sur lequel le serveur Java écoute

    // Créer le message à envoyer
    const message = `${Titre}${groupSeparator}${TMD}${groupSeparator}${date}${groupSeparator}Depenses${groupSeparator}reel${groupSeparator}d${groupSeparator}SSSSSSS`;
    const messageBuffer = Buffer.from(message);

    // Écouter la réponse du serveur
  client.on("message", (msg, rinfo) => {
    const msj = msg.toString(); // Message reçu du serveur
    client.close();

    // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau 
    const Datas = msj.split("\n");

    // Créer un tableau d'objets à partir des données
    const objects = Datas.map((Data) => {
      // Diviser les données  en fonction du séparateur (", ") pour obtenir les attributs
      const DataArray = Data.split(", ");

      // Créer un objet pour stocker les attributs 
      const object = {};
      // Parcourir les attributs et les ajouter à l'objet 
      DataArray.forEach((attr) => {
        // Vérifier si l'attribut contient ':'
        if (attr.includes(":")) {
          // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
          const [key, value] = attr.split(": ");
          // Ajouter l'attribut à l'objet 
          object[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
        } else {
          console.log("Invalid attribute format:", attr);
        }
      });

      return object;
    });
    resolve(objects); // Résoudre la promesse avec les utilisateurs récupérés
  });

    // Envoyer le message au serveur Java
    client.send(messageBuffer, serverPort, "localhost", (err) => {
      if (err) {
        console.error("Error while sending data:", err);
        client.close(); // Fermer le socket en cas d'erreur
        reject(err); // Rejeter la promesse en cas d'erreur
      } else {
          console.log(`Données ${Titre} et ${TMD} et ${date} envoyées avec succès !`);
      }
    });
  });
}


// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.getTotalMontantdepensePourChaqueTitreReel = getTotalMontantdepensePourChaqueTitreReel;




// Définition de la fonction pour envoyer les données au serveur Java
function getTotalMontantdepensePourChaqueCategorieReel(Categorie,TMD, date) {
  return new Promise((resolve, reject) => {
    const client = dgram.createSocket("udp4");
    const serverPort = 4000; // Port sur lequel le serveur Java écoute

    // Créer le message à envoyer
    const message = `${Categorie}${groupSeparator}${TMD}${groupSeparator}${date}${groupSeparator}Depenses${groupSeparator}reel${groupSeparator}e${groupSeparator}SSSSSSS`;
    const messageBuffer = Buffer.from(message);

    // Écouter la réponse du serveur
  client.on("message", (msg, rinfo) => {
    const msj = msg.toString(); // Message reçu du serveur
    client.close();

    // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau 
    const Datas = msj.split("\n");

    // Créer un tableau d'objets à partir des données
    const objects = Datas.map((Data) => {
      // Diviser les données  en fonction du séparateur (", ") pour obtenir les attributs
      const DataArray = Data.split(", ");

      // Créer un objet pour stocker les attributs 
      const object = {};
      // Parcourir les attributs et les ajouter à l'objet 
      DataArray.forEach((attr) => {
        // Vérifier si l'attribut contient ':'
        if (attr.includes(":")) {
          // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
          const [key, value] = attr.split(": ");
          // Ajouter l'attribut à l'objet 
          object[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
        } else {
          console.log("Invalid attribute format:", attr);
        }
      });

      return object;
    });
    resolve(objects); // Résoudre la promesse avec les utilisateurs récupérés
  });

    // Envoyer le message au serveur Java
    client.send(messageBuffer, serverPort, "localhost", (err) => {
      if (err) {
        console.error("Error while sending data:", err);
        client.close(); // Fermer le socket en cas d'erreur
        reject(err); // Rejeter la promesse en cas d'erreur
      } else {
          console.log(`Données ${Categorie} et ${TMD} et ${date} envoyées avec succès !`);
      }
    });
  });
}


// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.getTotalMontantdepensePourChaqueCategorieReel = getTotalMontantdepensePourChaqueCategorieReel;


            /************************************* Dépenses réel ************************************/



            /************************************* Dépenses Prévu ************************************/


// Définition de la fonction pour envoyer les données au serveur Java

function getTotalMontantdepensePourChaquePortefeuillePrevu(Portefeuille, TMD, date,date_prevu) {
  return new Promise((resolve, reject) => {
      const client = dgram.createSocket("udp4");
      let receivedObjects = [];
      const TOTAL_EXPECTED_OBJECTS = 2;
      const serverPort = 4000; // Port sur lequel le serveur Java écoute

      // Écouter la réponse du serveur
      client.on("message", (msg, rinfo) => {
    
          const msj = msg.toString(); // Message reçu du serveur

          // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau
          const Datas = msj.split("\n");

          // Créer un tableau d'objets à partir des données
          const objects = Datas.map((Data) => {
              // Diviser les données  en fonction du séparateur (", ") pour obtenir les attributs
              const DataArray = Data.split(", ");

              // Créer un objet pour stocker les attributs
              const object = {};
              // Parcourir les attributs et les ajouter à l'objet
              DataArray.forEach((attr) => {
                  // Vérifier si l'attribut contient ':'
                  if (attr.includes(":")) {
                     
                      // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
                      const [key, value] = attr.split(": ");
                      // Ajouter l'attribut à l'objet
                      object[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
                  } else {
                      console.log("Invalid attribute format:", attr);
                  }
              });

              return object;
          });

          // Ajouter le tableau d'objets actuel à la liste des tableaux reçus
          receivedObjects.push(objects);

          // Vérifier si tous les tableaux d'objets attendus ont été reçus
          if (receivedObjects.length === TOTAL_EXPECTED_OBJECTS) {
              // Résoudre la promesse avec tous les tableaux d'objets reçus
              resolve(receivedObjects);
          }
      });

      // Créer le message à envoyer
      const message = `${Portefeuille}${groupSeparator}${TMD}${groupSeparator}${date}${groupSeparator}Depenses${groupSeparator}prevu${groupSeparator}a${groupSeparator}${date_prevu}`;
      const messageBuffer = Buffer.from(message);

      // Envoyer le message au serveur Java
      client.send(messageBuffer, serverPort, "localhost", (err) => {
          if (err) {
              console.error("Error while sending data:", err);
              client.close(); // Fermer le socket en cas d'erreur
              reject(err); // Rejeter la promesse en cas d'erreur
          } else {
              console.log(`Données ${Portefeuille} et ${TMD} et ${date} et ${date_prevu} envoyées avec succès !`);
          }
      });
  });
}



// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.getTotalMontantdepensePourChaquePortefeuillePrevu = getTotalMontantdepensePourChaquePortefeuillePrevu;





// Définition de la fonction pour envoyer les données au serveur Java

function getTotalMontantdepensePourChaqueProgrammePrevu(Programme, TMD, date,date_prevu) {
  return new Promise((resolve, reject) => {
      const client = dgram.createSocket("udp4");
      let receivedObjects = [];
      const TOTAL_EXPECTED_OBJECTS = 2;
      const serverPort = 4000; // Port sur lequel le serveur Java écoute

      // Écouter la réponse du serveur
      client.on("message", (msg, rinfo) => {
    
          const msj = msg.toString(); // Message reçu du serveur

          // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau
          const Datas = msj.split("\n");

          // Créer un tableau d'objets à partir des données
          const objects = Datas.map((Data) => {
              // Diviser les données  en fonction du séparateur (", ") pour obtenir les attributs
              const DataArray = Data.split(", ");

              // Créer un objet pour stocker les attributs
              const object = {};
              // Parcourir les attributs et les ajouter à l'objet
              DataArray.forEach((attr) => {
                  // Vérifier si l'attribut contient ':'
                  if (attr.includes(":")) {
                     
                      // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
                      const [key, value] = attr.split(": ");
                      // Ajouter l'attribut à l'objet
                      object[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
                  } else {
                      console.log("Invalid attribute format:", attr);
                  }
              });

              return object;
          });

          // Ajouter le tableau d'objets actuel à la liste des tableaux reçus
          receivedObjects.push(objects);

          // Vérifier si tous les tableaux d'objets attendus ont été reçus
          if (receivedObjects.length === TOTAL_EXPECTED_OBJECTS) {
              // Résoudre la promesse avec tous les tableaux d'objets reçus
              resolve(receivedObjects);
          }
      });

      // Créer le message à envoyer
      const message = `${Programme}${groupSeparator}${TMD}${groupSeparator}${date}${groupSeparator}Depenses${groupSeparator}prevu${groupSeparator}b${groupSeparator}${date_prevu}`;
      const messageBuffer = Buffer.from(message);

      // Envoyer le message au serveur Java
      client.send(messageBuffer, serverPort, "localhost", (err) => {
          if (err) {
              console.error("Error while sending data:", err);
              client.close(); // Fermer le socket en cas d'erreur
              reject(err); // Rejeter la promesse en cas d'erreur
          } else {
              console.log(`Données ${Programme} et ${TMD} et ${date} et ${date_prevu} envoyées avec succès !`);
          }
      });
  });
}



// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.getTotalMontantdepensePourChaqueProgrammePrevu = getTotalMontantdepensePourChaqueProgrammePrevu;



// Définition de la fonction pour envoyer les données au serveur Java

function getTotalMontantdepensePourChaqueSousProgrammePrevu(SousProgramme, TMD, date,date_prevu) {
  return new Promise((resolve, reject) => {
      const client = dgram.createSocket("udp4");
      let receivedObjects = [];
      const TOTAL_EXPECTED_OBJECTS = 2;
      const serverPort = 4000; // Port sur lequel le serveur Java écoute

      // Écouter la réponse du serveur
      client.on("message", (msg, rinfo) => {
    
          const msj = msg.toString(); // Message reçu du serveur

          // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau
          const Datas = msj.split("\n");

          // Créer un tableau d'objets à partir des données
          const objects = Datas.map((Data) => {
              // Diviser les données  en fonction du séparateur (", ") pour obtenir les attributs
              const DataArray = Data.split(", ");

              // Créer un objet pour stocker les attributs
              const object = {};
              // Parcourir les attributs et les ajouter à l'objet
              DataArray.forEach((attr) => {
                  // Vérifier si l'attribut contient ':'
                  if (attr.includes(":")) {
                     
                      // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
                      const [key, value] = attr.split(": ");
                      // Ajouter l'attribut à l'objet
                      object[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
                  } else {
                      console.log("Invalid attribute format:", attr);
                  }
              });

              return object;
          });

          // Ajouter le tableau d'objets actuel à la liste des tableaux reçus
          receivedObjects.push(objects);

          // Vérifier si tous les tableaux d'objets attendus ont été reçus
          if (receivedObjects.length === TOTAL_EXPECTED_OBJECTS) {
              // Résoudre la promesse avec tous les tableaux d'objets reçus
              resolve(receivedObjects);
          }
      });

      // Créer le message à envoyer
      const message = `${SousProgramme}${groupSeparator}${TMD}${groupSeparator}${date}${groupSeparator}Depenses${groupSeparator}prevu${groupSeparator}c${groupSeparator}${date_prevu}`;
      const messageBuffer = Buffer.from(message);

      // Envoyer le message au serveur Java
      client.send(messageBuffer, serverPort, "localhost", (err) => {
          if (err) {
              console.error("Error while sending data:", err);
              client.close(); // Fermer le socket en cas d'erreur
              reject(err); // Rejeter la promesse en cas d'erreur
          } else {
              console.log(`Données ${SousProgramme} et ${TMD} et ${date} et ${date_prevu} envoyées avec succès !`);
          }
      });
  });
}



// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.getTotalMontantdepensePourChaqueSousProgrammePrevu = getTotalMontantdepensePourChaqueSousProgrammePrevu;


// Définition de la fonction pour envoyer les données au serveur Java

function getTotalMontantdepensePourChaqueTitrePrevu(Titre, TMD, date,date_prevu) {
  return new Promise((resolve, reject) => {
      const client = dgram.createSocket("udp4");
      let receivedObjects = [];
      const TOTAL_EXPECTED_OBJECTS = 2;
      const serverPort = 4000; // Port sur lequel le serveur Java écoute

      // Écouter la réponse du serveur
      client.on("message", (msg, rinfo) => {
    
          const msj = msg.toString(); // Message reçu du serveur

          // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau
          const Datas = msj.split("\n");

          // Créer un tableau d'objets à partir des données
          const objects = Datas.map((Data) => {
              // Diviser les données  en fonction du séparateur (", ") pour obtenir les attributs
              const DataArray = Data.split(", ");

              // Créer un objet pour stocker les attributs
              const object = {};
              // Parcourir les attributs et les ajouter à l'objet
              DataArray.forEach((attr) => {
                  // Vérifier si l'attribut contient ':'
                  if (attr.includes(":")) {
                     
                      // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
                      const [key, value] = attr.split(": ");
                      // Ajouter l'attribut à l'objet
                      object[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
                  } else {
                      console.log("Invalid attribute format:", attr);
                  }
              });

              return object;
          });

          // Ajouter le tableau d'objets actuel à la liste des tableaux reçus
          receivedObjects.push(objects);

          // Vérifier si tous les tableaux d'objets attendus ont été reçus
          if (receivedObjects.length === TOTAL_EXPECTED_OBJECTS) {
              // Résoudre la promesse avec tous les tableaux d'objets reçus
              resolve(receivedObjects);
          }
      });

      // Créer le message à envoyer
      const message = `${Titre}${groupSeparator}${TMD}${groupSeparator}${date}${groupSeparator}Depenses${groupSeparator}prevu${groupSeparator}d${groupSeparator}${date_prevu}`;
      const messageBuffer = Buffer.from(message);

      // Envoyer le message au serveur Java
      client.send(messageBuffer, serverPort, "localhost", (err) => {
          if (err) {
              console.error("Error while sending data:", err);
              client.close(); // Fermer le socket en cas d'erreur
              reject(err); // Rejeter la promesse en cas d'erreur
          } else {
              console.log(`Données ${Titre} et ${TMD} et ${date} et ${date_prevu} envoyées avec succès !`);
          }
      });
  });
}



// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.getTotalMontantdepensePourChaqueTitrePrevu = getTotalMontantdepensePourChaqueTitrePrevu;



// Définition de la fonction pour envoyer les données au serveur Java

function getTotalMontantdepensePourChaqueCategoriePrevu(Categorie, TMD, date,date_prevu) {
  return new Promise((resolve, reject) => {
      const client = dgram.createSocket("udp4");
      let receivedObjects = [];
      const TOTAL_EXPECTED_OBJECTS = 2;
      const serverPort = 4000; // Port sur lequel le serveur Java écoute

      // Écouter la réponse du serveur
      client.on("message", (msg, rinfo) => {
    
          const msj = msg.toString(); // Message reçu du serveur

          // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau
          const Datas = msj.split("\n");

          // Créer un tableau d'objets à partir des données
          const objects = Datas.map((Data) => {
              // Diviser les données  en fonction du séparateur (", ") pour obtenir les attributs
              const DataArray = Data.split(", ");

              // Créer un objet pour stocker les attributs
              const object = {};
              // Parcourir les attributs et les ajouter à l'objet
              DataArray.forEach((attr) => {
                  // Vérifier si l'attribut contient ':'
                  if (attr.includes(":")) {
                     
                      // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
                      const [key, value] = attr.split(": ");
                      // Ajouter l'attribut à l'objet
                      object[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
                  } else {
                      console.log("Invalid attribute format:", attr);
                  }
              });

              return object;
          });

          // Ajouter le tableau d'objets actuel à la liste des tableaux reçus
          receivedObjects.push(objects);

          // Vérifier si tous les tableaux d'objets attendus ont été reçus
          if (receivedObjects.length === TOTAL_EXPECTED_OBJECTS) {
              // Résoudre la promesse avec tous les tableaux d'objets reçus
              resolve(receivedObjects);
          }
      });

      // Créer le message à envoyer
      const message = `${Categorie}${groupSeparator}${TMD}${groupSeparator}${date}${groupSeparator}Depenses${groupSeparator}prevu${groupSeparator}e${groupSeparator}${date_prevu}`;
      const messageBuffer = Buffer.from(message);

      // Envoyer le message au serveur Java
      client.send(messageBuffer, serverPort, "localhost", (err) => {
          if (err) {
              console.error("Error while sending data:", err);
              client.close(); // Fermer le socket en cas d'erreur
              reject(err); // Rejeter la promesse en cas d'erreur
          } else {
              console.log(`Données ${Categorie} et ${TMD} et ${date} et ${date_prevu} envoyées avec succès !`);
          }
      });
  });
}



// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.getTotalMontantdepensePourChaqueCategoriePrevu = getTotalMontantdepensePourChaqueCategoriePrevu;




            /************************************* Dépenses Prévu ************************************/



            /************************************** Recette Réel *************************************/

// Définition de la fonction pour envoyer les données au serveur Java
function getTotalMontantPourChaqueCompteReel(Compte,TMD, date) {
  return new Promise((resolve, reject) => {
    const client = dgram.createSocket("udp4");
    const serverPort = 4000; // Port sur lequel le serveur Java écoute
  
    // Créer le message à envoyer
    const message = `${Compte}${groupSeparator}${TMD}${groupSeparator}${date}${groupSeparator}Recette${groupSeparator}reel${groupSeparator}a${groupSeparator}SSSSSSSS`;
    const messageBuffer = Buffer.from(message);
  
    // Écouter la réponse du serveur
  client.on("message", (msg, rinfo) => {
    const msj = msg.toString(); // Message reçu du serveur
    client.close();
  
    // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau 
    const Datas = msj.split("\n");
  
    // Créer un tableau d'objets à partir des données
    const objects = Datas.map((Data) => {
      // Diviser les données  en fonction du séparateur (", ") pour obtenir les attributs
      const DataArray = Data.split(", ");
  
      // Créer un objet pour stocker les attributs 
      const object = {};
      // Parcourir les attributs et les ajouter à l'objet 
      DataArray.forEach((attr) => {
        // Vérifier si l'attribut contient ':'
        if (attr.includes(":")) {
          // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
          const [key, value] = attr.split(": ");
          // Ajouter l'attribut à l'objet 
          object[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
        } else {
          console.log("Invalid attribute format:", attr);
        }
      });
  
      return object;
    });
    resolve(objects); // Résoudre la promesse avec les utilisateurs récupérés
  });
  
    // Envoyer le message au serveur Java
    client.send(messageBuffer, serverPort, "localhost", (err) => {
      if (err) {
        console.error("Error while sending data:", err);
        client.close(); // Fermer le socket en cas d'erreur
        reject(err); // Rejeter la promesse en cas d'erreur
      } else {
          console.log(`Données ${Compte} et ${TMD} et ${date} envoyées avec succès !`);
      }
    });
  });
  }
  
  
  // Exporter la fonction pour l'utiliser dans d'autres modules
  module.exports.getTotalMontantPourChaqueCompteReel = getTotalMontantPourChaqueCompteReel;



  // Définition de la fonction pour envoyer les données au serveur Java
function getTotalMontantPourChaquePosteComptableReel(PosteComptable,TMD, birthday) {
  return new Promise((resolve, reject) => {
    const client = dgram.createSocket("udp4");
    const serverPort = 4000; // Port sur lequel le serveur Java écoute
  
    // Créer le message à envoyer
    const message = `${PosteComptable}${groupSeparator}${TMD}${groupSeparator}${birthday}${groupSeparator}Recette${groupSeparator}reel${groupSeparator}b${groupSeparator}SSSSSSSS`;
    const messageBuffer = Buffer.from(message);
  
    // Écouter la réponse du serveur
  client.on("message", (msg, rinfo) => {
    const msj = msg.toString(); // Message reçu du serveur
    client.close();
  
    // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau 
    const Datas = msj.split("\n");
  
    // Créer un tableau d'objets à partir des données
    const objects = Datas.map((Data) => {
      // Diviser les données  en fonction du séparateur (", ") pour obtenir les attributs
      const DataArray = Data.split(", ");
  
      // Créer un objet pour stocker les attributs 
      const object = {};
      // Parcourir les attributs et les ajouter à l'objet 
      DataArray.forEach((attr) => {
        // Vérifier si l'attribut contient ':'
        if (attr.includes(":")) {
          // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
          const [key, value] = attr.split(": ");
          // Ajouter l'attribut à l'objet 
          object[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
        } else {
          console.log("Invalid attribute format:", attr);
        }
      });
  
      return object;
    });
    resolve(objects); // Résoudre la promesse avec les utilisateurs récupérés
  });
  
    // Envoyer le message au serveur Java
    client.send(messageBuffer, serverPort, "localhost", (err) => {
      if (err) {
        console.error("Error while sending data:", err);
        client.close(); // Fermer le socket en cas d'erreur
        reject(err); // Rejeter la promesse en cas d'erreur
      } else {
          console.log(`Données ${PosteComptable} et ${TMD} et ${birthday} envoyées avec succès !`);
      }
    });
  });
  }
  
  
  // Exporter la fonction pour l'utiliser dans d'autres modules
  module.exports.getTotalMontantPourChaquePosteComptableReel = getTotalMontantPourChaquePosteComptableReel;



        /************************************** Recette Réel *************************************/


         /************************************** Recette Prévu *************************************/
     // Définition de la fonction pour envoyer les données au serveur Java

function getTotalMontantPourChaqueComptePrevu(Compte, TMD, date,date_prevu) {
  return new Promise((resolve, reject) => {
      const client = dgram.createSocket("udp4");
      let receivedObjects = [];
      const TOTAL_EXPECTED_OBJECTS = 2;
      const serverPort = 4000; // Port sur lequel le serveur Java écoute

      // Écouter la réponse du serveur
      client.on("message", (msg, rinfo) => {
    
          const msj = msg.toString(); // Message reçu du serveur

          // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau
          const Datas = msj.split("\n");

          // Créer un tableau d'objets à partir des données
          const objects = Datas.map((Data) => {
              // Diviser les données  en fonction du séparateur (", ") pour obtenir les attributs
              const DataArray = Data.split(", ");

              // Créer un objet pour stocker les attributs
              const object = {};
              // Parcourir les attributs et les ajouter à l'objet
              DataArray.forEach((attr) => {
                  // Vérifier si l'attribut contient ':'
                  if (attr.includes(":")) {
                     
                      // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
                      const [key, value] = attr.split(": ");
                      // Ajouter l'attribut à l'objet
                      object[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
                  } else {
                      console.log("Invalid attribute format:", attr);
                  }
              });

              return object;
          });

          // Ajouter le tableau d'objets actuel à la liste des tableaux reçus
          receivedObjects.push(objects);

          // Vérifier si tous les tableaux d'objets attendus ont été reçus
          if (receivedObjects.length === TOTAL_EXPECTED_OBJECTS) {
              // Résoudre la promesse avec tous les tableaux d'objets reçus
              resolve(receivedObjects);
          }
      });

      // Créer le message à envoyer
      const message = `${Compte}${groupSeparator}${TMD}${groupSeparator}${date}${groupSeparator}Recette${groupSeparator}prevu${groupSeparator}a${groupSeparator}${date_prevu}`;
      const messageBuffer = Buffer.from(message);

      // Envoyer le message au serveur Java
      client.send(messageBuffer, serverPort, "localhost", (err) => {
          if (err) {
              console.error("Error while sending data:", err);
              client.close(); // Fermer le socket en cas d'erreur
              reject(err); // Rejeter la promesse en cas d'erreur
          } else {
              console.log(`Données ${Compte} et ${TMD} et ${date} et ${date_prevu} envoyées avec succès !`);
          }
      });
  });
}



// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.getTotalMontantPourChaqueComptePrevu = getTotalMontantPourChaqueComptePrevu;
     
  



// Définition de la fonction pour envoyer les données au serveur Java

function getTotalMontantPourChaquePosteComptablePrevu(PosteComptable, TMD, date,date_prevu) {
  return new Promise((resolve, reject) => {
      const client = dgram.createSocket("udp4");
      let receivedObjects = [];
      const TOTAL_EXPECTED_OBJECTS = 2;
      const serverPort = 4000; // Port sur lequel le serveur Java écoute

      // Écouter la réponse du serveur
      client.on("message", (msg, rinfo) => {
    
          const msj = msg.toString(); // Message reçu du serveur

          // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau
          const Datas = msj.split("\n");

          // Créer un tableau d'objets à partir des données
          const objects = Datas.map((Data) => {
              // Diviser les données  en fonction du séparateur (", ") pour obtenir les attributs
              const DataArray = Data.split(", ");

              // Créer un objet pour stocker les attributs
              const object = {};
              // Parcourir les attributs et les ajouter à l'objet
              DataArray.forEach((attr) => {
                  // Vérifier si l'attribut contient ':'
                  if (attr.includes(":")) {
                     
                      // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
                      const [key, value] = attr.split(": ");
                      // Ajouter l'attribut à l'objet
                      object[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
                  } else {
                      console.log("Invalid attribute format:", attr);
                  }
              });

              return object;
          });

          // Ajouter le tableau d'objets actuel à la liste des tableaux reçus
          receivedObjects.push(objects);

          // Vérifier si tous les tableaux d'objets attendus ont été reçus
          if (receivedObjects.length === TOTAL_EXPECTED_OBJECTS) {
              // Résoudre la promesse avec tous les tableaux d'objets reçus
              resolve(receivedObjects);
          }
      });

      // Créer le message à envoyer
      const message = `${PosteComptable}${groupSeparator}${TMD}${groupSeparator}${date}${groupSeparator}Recette${groupSeparator}prevu${groupSeparator}b${groupSeparator}${date_prevu}`;
      const messageBuffer = Buffer.from(message);

      // Envoyer le message au serveur Java
      client.send(messageBuffer, serverPort, "localhost", (err) => {
          if (err) {
              console.error("Error while sending data:", err);
              client.close(); // Fermer le socket en cas d'erreur
              reject(err); // Rejeter la promesse en cas d'erreur
          } else {
              console.log(`Données ${PosteComptable} et ${TMD} et ${date} et ${date_prevu} envoyées avec succès !`);
          }
      });
  });
}



// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.getTotalMontantPourChaquePosteComptablePrevu = getTotalMontantPourChaquePosteComptablePrevu;



   /************************************** Recette Prévu *************************************/


    /************************************** Dette Réel *************************************/

// Définition de la fonction pour envoyer les données au serveur Java
function getTotalMontantRembourcePourChaqueSoumissionaireReel(Soumissionaire,TMD,date) {
  return new Promise((resolve, reject) => {
    const client = dgram.createSocket("udp4");
    const serverPort = 4000; // Port sur lequel le serveur Java écoute
  
    // Créer le message à envoyer
    const message = `${Soumissionaire}${groupSeparator}${TMD}${groupSeparator}${date}${groupSeparator}Dette${groupSeparator}reel${groupSeparator}a${groupSeparator}SSSSSSSS`;
    const messageBuffer = Buffer.from(message);
  
    // Écouter la réponse du serveur
  client.on("message", (msg, rinfo) => {
    const msj = msg.toString(); // Message reçu du serveur
    client.close();
  
    // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau 
    const Datas = msj.split("\n");
  
    // Créer un tableau d'objets à partir des données
    const objects = Datas.map((Data) => {
      // Diviser les données  en fonction du séparateur (", ") pour obtenir les attributs
      const DataArray = Data.split(", ");
  
      // Créer un objet pour stocker les attributs 
      const object = {};
      // Parcourir les attributs et les ajouter à l'objet 
      DataArray.forEach((attr) => {
        // Vérifier si l'attribut contient ':'
        if (attr.includes(":")) {
          // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
          const [key, value] = attr.split(": ");
          // Ajouter l'attribut à l'objet 
          object[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
        } else {
          console.log("Invalid attribute format:", attr);
        }
      });
  
      return object;
    });
    resolve(objects); // Résoudre la promesse avec les utilisateurs récupérés
  });
  
    // Envoyer le message au serveur Java
    client.send(messageBuffer, serverPort, "localhost", (err) => {
      if (err) {
        console.error("Error while sending data:", err);
        client.close(); // Fermer le socket en cas d'erreur
        reject(err); // Rejeter la promesse en cas d'erreur
      } else {
          console.log(`Données ${Soumissionaire} et ${TMD} et ${date} envoyées avec succès !`);
      }
    });
  });
  }
  
  
  // Exporter la fonction pour l'utiliser dans d'autres modules
  module.exports.getTotalMontantRembourcePourChaqueSoumissionaireReel = getTotalMontantRembourcePourChaqueSoumissionaireReel;



  // Définition de la fonction pour envoyer les données au serveur Java
function getTotalMontantRembourcePourChaqueTitreReel(Titre,TMD,date) {
  return new Promise((resolve, reject) => {
    const client = dgram.createSocket("udp4");
    const serverPort = 4000; // Port sur lequel le serveur Java écoute
  
    // Créer le message à envoyer
    const message = `${Titre}${groupSeparator}${TMD}${groupSeparator}${date}${groupSeparator}Dette${groupSeparator}reel${groupSeparator}b${groupSeparator}SSSSSSSS`;
    const messageBuffer = Buffer.from(message);
  
    // Écouter la réponse du serveur
  client.on("message", (msg, rinfo) => {
    const msj = msg.toString(); // Message reçu du serveur
    client.close();
  
    // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau 
    const Datas = msj.split("\n");
  
    // Créer un tableau d'objets à partir des données
    const objects = Datas.map((Data) => {
      // Diviser les données  en fonction du séparateur (", ") pour obtenir les attributs
      const DataArray = Data.split(", ");
  
      // Créer un objet pour stocker les attributs 
      const object = {};
      // Parcourir les attributs et les ajouter à l'objet 
      DataArray.forEach((attr) => {
        // Vérifier si l'attribut contient ':'
        if (attr.includes(":")) {
          // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
          const [key, value] = attr.split(": ");
          // Ajouter l'attribut à l'objet 
          object[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
        } else {
          console.log("Invalid attribute format:", attr);
        }
      });
  
      return object;
    });
    resolve(objects); // Résoudre la promesse avec les utilisateurs récupérés
  });
  
    // Envoyer le message au serveur Java
    client.send(messageBuffer, serverPort, "localhost", (err) => {
      if (err) {
        console.error("Error while sending data:", err);
        client.close(); // Fermer le socket en cas d'erreur
        reject(err); // Rejeter la promesse en cas d'erreur
      } else {
          console.log(`Données ${Titre} et ${TMD} et ${date} envoyées avec succès !`);
      }
    });
  });
  }
  
  
  // Exporter la fonction pour l'utiliser dans d'autres modules
  module.exports.getTotalMontantRembourcePourChaqueTitreReel = getTotalMontantRembourcePourChaqueTitreReel;
        /************************************** Dette Réel *************************************/

        /************************************** Dette Prévu ************************************/


        // Définition de la fonction pour envoyer les données au serveur Java

function getTotalMontantRembourcePourChaqueSoumissionairePrevu(Soumissionaire, TMD, date,date_prevu) {
  return new Promise((resolve, reject) => {
      const client = dgram.createSocket("udp4");
      let receivedObjects = [];
      const TOTAL_EXPECTED_OBJECTS = 2;
      const serverPort = 4000; // Port sur lequel le serveur Java écoute

      // Écouter la réponse du serveur
      client.on("message", (msg, rinfo) => {
    
          const msj = msg.toString(); // Message reçu du serveur

          // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau
          const Datas = msj.split("\n");

          // Créer un tableau d'objets à partir des données
          const objects = Datas.map((Data) => {
              // Diviser les données  en fonction du séparateur (", ") pour obtenir les attributs
              const DataArray = Data.split(", ");

              // Créer un objet pour stocker les attributs
              const object = {};
              // Parcourir les attributs et les ajouter à l'objet
              DataArray.forEach((attr) => {
                  // Vérifier si l'attribut contient ':'
                  if (attr.includes(":")) {
                     
                      // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
                      const [key, value] = attr.split(": ");
                      // Ajouter l'attribut à l'objet
                      object[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
                  } else {
                      console.log("Invalid attribute format:", attr);
                  }
              });

              return object;
          });

          // Ajouter le tableau d'objets actuel à la liste des tableaux reçus
          receivedObjects.push(objects);

          // Vérifier si tous les tableaux d'objets attendus ont été reçus
          if (receivedObjects.length === TOTAL_EXPECTED_OBJECTS) {
              // Résoudre la promesse avec tous les tableaux d'objets reçus
              resolve(receivedObjects);
          }
      });

      // Créer le message à envoyer
      const message = `${Soumissionaire}${groupSeparator}${TMD}${groupSeparator}${date}${groupSeparator}Dette${groupSeparator}prevu${groupSeparator}a${groupSeparator}${date_prevu}`;
      const messageBuffer = Buffer.from(message);

      // Envoyer le message au serveur Java
      client.send(messageBuffer, serverPort, "localhost", (err) => {
          if (err) {
              console.error("Error while sending data:", err);
              client.close(); // Fermer le socket en cas d'erreur
              reject(err); // Rejeter la promesse en cas d'erreur
          } else {
              console.log(`Données ${Soumissionaire} et ${TMD} et ${date} et ${date_prevu} envoyées avec succès !`);
          }
      });
  });
}



// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.getTotalMontantRembourcePourChaqueSoumissionairePrevu = getTotalMontantRembourcePourChaqueSoumissionairePrevu;



// Définition de la fonction pour envoyer les données au serveur Java

function getTotalMontantRembourcePourChaqueTitrePrevu(Titre, TMD, date,date_prevu) {
  return new Promise((resolve, reject) => {
      const client = dgram.createSocket("udp4");
      let receivedObjects = [];
      const TOTAL_EXPECTED_OBJECTS = 2;
      const serverPort = 4000; // Port sur lequel le serveur Java écoute

      // Écouter la réponse du serveur
      client.on("message", (msg, rinfo) => {
    
          const msj = msg.toString(); // Message reçu du serveur

          // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau
          const Datas = msj.split("\n");

          // Créer un tableau d'objets à partir des données
          const objects = Datas.map((Data) => {
              // Diviser les données  en fonction du séparateur (", ") pour obtenir les attributs
              const DataArray = Data.split(", ");

              // Créer un objet pour stocker les attributs
              const object = {};
              // Parcourir les attributs et les ajouter à l'objet
              DataArray.forEach((attr) => {
                  // Vérifier si l'attribut contient ':'
                  if (attr.includes(":")) {
                     
                      // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
                      const [key, value] = attr.split(": ");
                      // Ajouter l'attribut à l'objet
                      object[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
                  } else {
                      console.log("Invalid attribute format:", attr);
                  }
              });

              return object;
          });

          // Ajouter le tableau d'objets actuel à la liste des tableaux reçus
          receivedObjects.push(objects);

          // Vérifier si tous les tableaux d'objets attendus ont été reçus
          if (receivedObjects.length === TOTAL_EXPECTED_OBJECTS) {
              // Résoudre la promesse avec tous les tableaux d'objets reçus
              resolve(receivedObjects);
          }
      });

      // Créer le message à envoyer
      const message = `${Titre}${groupSeparator}${TMD}${groupSeparator}${date}${groupSeparator}Dette${groupSeparator}prevu${groupSeparator}b${groupSeparator}${date_prevu}`;
      const messageBuffer = Buffer.from(message);

      // Envoyer le message au serveur Java
      client.send(messageBuffer, serverPort, "localhost", (err) => {
          if (err) {
              console.error("Error while sending data:", err);
              client.close(); // Fermer le socket en cas d'erreur
              reject(err); // Rejeter la promesse en cas d'erreur
          } else {
              console.log(`Données ${Titre} et ${TMD} et ${date} et ${date_prevu} envoyées avec succès !`);
          }
      });
  });
}



// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.getTotalMontantRembourcePourChaqueTitrePrevu = getTotalMontantRembourcePourChaqueTitrePrevu;


          /************************************** Dette Prévu ************************************/

          /************************************** Dashbord  **************************************/


// Définition de la fonction pour envoyer les données au serveur Java

function getEtatTresorActually(date) {
  return new Promise((resolve, reject) => {
      const client = dgram.createSocket("udp4");
      let receivedObjects = [];
      const TOTAL_EXPECTED_OBJECTS = 4;
      const serverPort = 4000; // Port sur lequel le serveur Java écoute

      // Écouter la réponse du serveur
      client.on("message", (msg, rinfo) => {
    
          const msj = msg.toString(); // Message reçu du serveur

          // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau
          const Datas = msj.split("\n");

          // Créer un tableau d'objets à partir des données
          const objects = Datas.map((Data) => {
              // Diviser les données  en fonction du séparateur (", ") pour obtenir les attributs
              const DataArray = Data.split(", ");

              // Créer un objet pour stocker les attributs
              const object = {};
              // Parcourir les attributs et les ajouter à l'objet
              DataArray.forEach((attr) => {
                  // Vérifier si l'attribut contient ':'
                  if (attr.includes(":")) {
                     
                      // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
                      const [key, value] = attr.split(": ");
                      // Ajouter l'attribut à l'objet
                      object[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
                  } else {
                      console.log("Invalid attribute format:", attr);
                  }
              });

              return object;
          });

          // Ajouter le tableau d'objets actuel à la liste des tableaux reçus
          receivedObjects.push(objects);

          // Vérifier si tous les tableaux d'objets attendus ont été reçus
          if (receivedObjects.length === TOTAL_EXPECTED_OBJECTS) {
              // Résoudre la promesse avec tous les tableaux d'objets reçus
              resolve(receivedObjects);
          }
      });

      // Créer le message à envoyer
      const message = `${date}${groupSeparator}Reel`;
      const messageBuffer = Buffer.from(message);

      // Envoyer le message au serveur Java
      client.send(messageBuffer, serverPort, "localhost", (err) => {
          if (err) {
              console.error("Error while sending data:", err);
              client.close(); // Fermer le socket en cas d'erreur
              reject(err); // Rejeter la promesse en cas d'erreur
          } else {
              console.log(`Données ${date} envoyées avec succès !`);
          }
      });
  });
}



// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.getEtatTresorActually = getEtatTresorActually;


/************************************** Dashbord  **************************************/


/************************************** Tables  **************************************/


// Définition de la fonction pour envoyer les données au serveur Java
function GetAllActions() {
  return new Promise((resolve, reject) => {
    const client = dgram.createSocket("udp4");
    const serverPort = 4000; // Port sur lequel le serveur Java écoute

    // Créer le message à envoyer
    const message = `All${groupSeparator}Actions`;
    const messageBuffer = Buffer.from(message);

    // Écouter la réponse du serveur
    client.on("message", (msg, rinfo) => {
      const msj = msg.toString(); // Message reçu du serveur
      client.close();

      // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau d'actions
      const usersData = msj.split("\n");

      // Créer un tableau d'objets à partir des données
      const users = usersData.map((userData) => {
        // Diviser les données de l'action en fonction du séparateur (", ") pour obtenir les attributs
        const userDataArray = userData.split(", ");

        // Créer un objet pour stocker les attributs de l'action
        const user = {};
        // Parcourir les attributs et les ajouter à l'objet action
        userDataArray.forEach((attr) => {
          // Vérifier si l'attribut contient ':'
          if (attr.includes(":")) {
            // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
            const [key, value] = attr.split(": ");
            // Ajouter l'attribut à l'objet user
            user[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
          } else {
            console.log("Invalid attribute format:", attr);
          }
        });

        return user;
      });
      resolve(users); // Résoudre la promesse avec les Actions récupérés
    });

    // Envoyer le message au serveur Java
    client.send(messageBuffer, serverPort, "localhost", (err) => {
      if (err) {
        console.error("Error while sending data:", err);
        client.close(); // Fermer le socket en cas d'erreur
        reject(err); // Rejeter la promesse en cas d'erreur
      } else {
        console.log("get All Actions sent successfully");
      }
    });
  });
}


// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.GetAllActions = GetAllActions;
          

// Définition de la fonction pour envoyer les données au serveur Java
function GetAllWilayas() {
  return new Promise((resolve, reject) => {
    const client = dgram.createSocket("udp4");
    const serverPort = 4000; // Port sur lequel le serveur Java écoute

    // Créer le message à envoyer
    const message = `All${groupSeparator}Wilaya`;
    const messageBuffer = Buffer.from(message);

    // Écouter la réponse du serveur
    client.on("message", (msg, rinfo) => {
      const msj = msg.toString(); // Message reçu du serveur
      client.close();

      // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau d'actions
      const usersData = msj.split("\n");

      // Créer un tableau d'objets à partir des données
      const users = usersData.map((userData) => {
        // Diviser les données de l'action en fonction du séparateur (", ") pour obtenir les attributs
        const userDataArray = userData.split(", ");

        // Créer un objet pour stocker les attributs de l'action
        const user = {};
        // Parcourir les attributs et les ajouter à l'objet action
        userDataArray.forEach((attr) => {
          // Vérifier si l'attribut contient ':'
          if (attr.includes(":")) {
            // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
            const [key, value] = attr.split(": ");
            // Ajouter l'attribut à l'objet user
            user[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
          } else {
            console.log("Invalid attribute format:", attr);
          }
        });

        return user;
      });
      resolve(users); // Résoudre la promesse avec les Actions récupérés
    });

    // Envoyer le message au serveur Java
    client.send(messageBuffer, serverPort, "localhost", (err) => {
      if (err) {
        console.error("Error while sending data:", err);
        client.close(); // Fermer le socket en cas d'erreur
        reject(err); // Rejeter la promesse en cas d'erreur
      } else {
        console.log("get All Wilayas sent successfully");
      }
    });
  });
}


// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.GetAllWilayas = GetAllWilayas;



// Définition de la fonction pour envoyer les données au serveur Java
function GetAllDettes() {
  return new Promise((resolve, reject) => {
    const client = dgram.createSocket("udp4");
    const serverPort = 4000; // Port sur lequel le serveur Java écoute

    // Créer le message à envoyer
    const message = `All${groupSeparator}Dette`;
    const messageBuffer = Buffer.from(message);

    // Écouter la réponse du serveur
    client.on("message", (msg, rinfo) => {
      const msj = msg.toString(); // Message reçu du serveur
      client.close();

      // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau d'actions
      const usersData = msj.split("\n");

      // Créer un tableau d'objets à partir des données
      const users = usersData.map((userData) => {
        // Diviser les données de l'action en fonction du séparateur (", ") pour obtenir les attributs
        const userDataArray = userData.split(", ");

        // Créer un objet pour stocker les attributs de l'action
        const user = {};
        // Parcourir les attributs et les ajouter à l'objet action
        userDataArray.forEach((attr) => {
          // Vérifier si l'attribut contient ':'
          if (attr.includes(":")) {
            // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
            const [key, value] = attr.split(": ");
            // Ajouter l'attribut à l'objet user
            user[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
          } else {
            console.log("Invalid attribute format:", attr);
          }
        });

        return user;
      });
      resolve(users); // Résoudre la promesse avec les Actions récupérés
    });

    // Envoyer le message au serveur Java
    client.send(messageBuffer, serverPort, "localhost", (err) => {
      if (err) {
        console.error("Error while sending data:", err);
        client.close(); // Fermer le socket en cas d'erreur
        reject(err); // Rejeter la promesse en cas d'erreur
      } else {
        console.log("get All Dettes sent successfully");
      }
    });
  });
}


// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.GetAllDettes = GetAllDettes;



// Définition de la fonction pour envoyer les données au serveur Java
function GetAllCredits() {
  return new Promise((resolve, reject) => {
    const client = dgram.createSocket("udp4");
    const serverPort = 4000; // Port sur lequel le serveur Java écoute

    // Créer le message à envoyer
    const message = `All${groupSeparator}Credit`;
    const messageBuffer = Buffer.from(message);

    // Écouter la réponse du serveur
    client.on("message", (msg, rinfo) => {
      const msj = msg.toString(); // Message reçu du serveur
      client.close();

      // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau d'actions
      const usersData = msj.split("\n");

      // Créer un tableau d'objets à partir des données
      const users = usersData.map((userData) => {
        // Diviser les données de l'action en fonction du séparateur (", ") pour obtenir les attributs
        const userDataArray = userData.split(", ");

        // Créer un objet pour stocker les attributs de l'action
        const user = {};
        // Parcourir les attributs et les ajouter à l'objet action
        userDataArray.forEach((attr) => {
          // Vérifier si l'attribut contient ':'
          if (attr.includes(":")) {
            // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
            const [key, value] = attr.split(": ");
            // Ajouter l'attribut à l'objet user
            user[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
          } else {
            console.log("Invalid attribute format:", attr);
          }
        });

        return user;
      });
      resolve(users); // Résoudre la promesse avec les Actions récupérés
    });

    // Envoyer le message au serveur Java
    client.send(messageBuffer, serverPort, "localhost", (err) => {
      if (err) {
        console.error("Error while sending data:", err);
        client.close(); // Fermer le socket en cas d'erreur
        reject(err); // Rejeter la promesse en cas d'erreur
      } else {
        console.log("get All Credits sent successfully");
      }
    });
  });
}


// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.GetAllCredits = GetAllCredits;




// Définition de la fonction pour envoyer les données au serveur Java
function GetAllRecette() {
  return new Promise((resolve, reject) => {
    const client = dgram.createSocket("udp4");
    const serverPort = 4000; // Port sur lequel le serveur Java écoute

    // Créer le message à envoyer
    const message = `All${groupSeparator}Recette`;
    const messageBuffer = Buffer.from(message);

    // Écouter la réponse du serveur
    client.on("message", (msg, rinfo) => {
      const msj = msg.toString(); // Message reçu du serveur
      client.close();

      // Diviser la chaîne en fonction des sauts de ligne pour obtenir un tableau d'actions
      const usersData = msj.split("\n");

      // Créer un tableau d'objets à partir des données
      const users = usersData.map((userData) => {
        // Diviser les données de l'action en fonction du séparateur (", ") pour obtenir les attributs
        const userDataArray = userData.split(", ");

        // Créer un objet pour stocker les attributs de l'action
        const user = {};
        // Parcourir les attributs et les ajouter à l'objet action
        userDataArray.forEach((attr) => {
          // Vérifier si l'attribut contient ':'
          if (attr.includes(":")) {
            // Diviser l'attribut en clé et valeur en fonction du séparateur (": ")
            const [key, value] = attr.split(": ");
            // Ajouter l'attribut à l'objet user
            user[key.trim()] = value.trim(); // Supprimer les espaces autour de la clé et de la valeur
          } else {
            console.log("Invalid attribute format:", attr);
          }
        });

        return user;
      });
      resolve(users); // Résoudre la promesse avec les Actions récupérés
    });

    // Envoyer le message au serveur Java
    client.send(messageBuffer, serverPort, "localhost", (err) => {
      if (err) {
        console.error("Error while sending data:", err);
        client.close(); // Fermer le socket en cas d'erreur
        reject(err); // Rejeter la promesse en cas d'erreur
      } else {
        console.log("get All Recettes sent successfully");
      }
    });
  });
}


// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.GetAllRecette = GetAllRecette;