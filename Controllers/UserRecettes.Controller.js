const dgram = require("dgram");
const groupSeparator = '\u001D';

/************************************************ Requetes SQL ****************************************************/


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

