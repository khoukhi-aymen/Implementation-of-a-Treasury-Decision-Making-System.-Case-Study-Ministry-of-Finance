/******************************** register ***************************/

const dgram = require('dgram');
const groupSeparator = '\u001D';

// Définition de la fonction pour envoyer les données au serveur Java
function sendDataToServer(name, email, password) {
    return new Promise((resolve, reject) => {
        const client = dgram.createSocket('udp4');
        const serverPort = 4000; // Port sur lequel le serveur Java écoute

        // Créer le message à envoyer
        const message = `${name}${groupSeparator}${email}${groupSeparator}${password}`;
        const messageBuffer = Buffer.from(message);

        // Envoyer le message au serveur Java
        client.send(messageBuffer, serverPort, 'localhost', (err) => {
            if (err) {
                console.error('Error while sending data:', err);
                reject(err); // Rejeter la promesse en cas d'erreur
            } else {
                console.log('Data sent successfully');

                // Écouter la réponse du serveur
                client.on('message', (msg, rinfo) => {
                    console.log(`Received response from server Register: ${msg.toString()}`);
                    const msj = msg.toString(); // Message reçu du serveur
                    client.close();
                    //resolvedMessage = msj;
                    resolve(msj)
                    //resolve(msj); // Résoudre la promesse avec le message du serveur
                });
            }
        });
    });
}

// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.sendDataToServer = sendDataToServer;






exports.getRegisterPage=(req,res,next)=>{

    // Rendez la page d'inscription en passant le message flash
    res.render('Connectez-vous', { msj: '',role:''});
}

/******************************** register ***************************/

/******************************** login ******************************/


exports.getLoginPage=(req,res,next)=>{
    res.render('Connectez-vous',{msj:'',role:''});
}



// Définition de la fonction pour envoyer les données au serveur Java
function sendDataLoginToServer(mail, psw) {
    return new Promise((resolve, reject) => {
        const dgram = require('dgram');
        const client = dgram.createSocket('udp4');
        const serverPort = 4000; // Port sur lequel le serveur Java écoute

        // Créer le message à envoyer
        const message = `${mail}${groupSeparator}${psw}${groupSeparator}login`;
        const messageBuffer = Buffer.from(message);

        // Envoyer le message au serveur Java
        client.send(messageBuffer, serverPort, 'localhost', (err) => {
            if (err) {
                console.error('Error while sending data:', err);
                client.close();
                reject(err); // Rejeter la promesse en cas d'erreur d'envoi
            } else {
                console.log('Data envoyées avec succés');
            }
        });

        // Écouter la réponse du serveur
        client.on('message', (msg, rinfo) => {
            console.log(`Received response from server Login: ${msg.toString()}`);
            // Ici, vous pouvez traiter la réponse reçue du serveur
            // Par exemple, vous pouvez convertir le message en JSON et l'utiliser dans votre application
            const role = msg.toString(); // Supposant que le serveur renvoie le role de l'utilisateur
            // Utilisez l'ID de l'utilisateur comme vous le souhaitez dans votre application
            client.close();
            resolve(role); // Résoudre la promesse avec le role de l'utilisateur
        });
    });
}

// Exporter la fonction pour l'utiliser dans d'autres modules
module.exports.sendDataLoginToServer = sendDataLoginToServer;

/******************************** login ******************************/


/******************************** logout ******************************/
exports.logoutFunctionController = (req,res,next)=>{

    res.render('Connectez-vous',{ msj: '',role:''});
}
/******************************** logout ******************************/

