
const dgram = require("dgram");
const groupSeparator = '\u001D';
// Définition de la fonction pour envoyer les données au serveur Java
function ETLFichier(key,stringifiedArray) {
    return new Promise((resolve, reject) => {
      const client = dgram.createSocket("udp4");
      const serverPort = 4000; // Port sur lequel le serveur Java écoute

      // Créer le message à envoyer
      const message = `${key}${groupSeparator}${stringifiedArray}${groupSeparator}ETL${groupSeparator}Fichier`;
      //console.log(typeof stringifiedArray);
      //console.log(typeof message);
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
            console.log(`Données ${key} pour l'ETL envoyées avec succès !`);
        }
      });
    });
  }
  
  
  // Exporter la fonction pour l'utiliser dans d'autres modules
  module.exports.ETLFichier = ETLFichier;



  // Définition de la fonction pour envoyer les données au serveur Java
function ETLFormulaire(a, b,c,d,e,f,g,h,i,j,k,l,m,n) {
  return new Promise((resolve, reject) => {
    const client = dgram.createSocket("udp4");
    const serverPort = 4000; // Port sur lequel le serveur Java écoute

    // Créer le message à envoyer
    const message = `${a}${groupSeparator}${b}${groupSeparator}${c}${groupSeparator}${d}${groupSeparator}${e}${groupSeparator}${f}${groupSeparator}${g}${groupSeparator}${h}${groupSeparator}${i}${groupSeparator}${j}${groupSeparator}${k}${groupSeparator}${l}${groupSeparator}${m}${groupSeparator}${n}`;
    //console.log(typeof stringifiedArray);
    //console.log(typeof message);
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
          console.log(`Données ${a} et ${b} et ${c} pour l'ETL envoyées avec succès !`);
          console.log(`Données ${d} et ${e} et ${f} pour l'ETL envoyées avec succès !`);
          console.log(`Données ${g} et ${h} et ${i}  pour l'ETL envoyées avec succès !`);
          console.log(`Données ${j} et ${k} et ${l}  pour l'ETL envoyées avec succès !`);
          console.log(`Données ${m} et ${n}  pour l'ETL envoyées avec succès !`);
      }
    });
  });
}


// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.ETLFormulaire = ETLFormulaire;