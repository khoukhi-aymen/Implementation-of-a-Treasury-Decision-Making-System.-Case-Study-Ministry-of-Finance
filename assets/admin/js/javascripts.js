// Fonction pour dessiner l'histogramme
function drawHistogram(data) {
    const histogramContainer = document.getElementById('en_aire_chart');
    
    // Effacer le contenu précédent
    histogramContainer.innerHTML = '';
  
    // Créer les barres de l'histogramme en fonction des données
    data.forEach(value => {
      const bar = document.createElement('div');
      bar.classList.add('bar');
      bar.style.height = value + 'px';
      histogramContainer.appendChild(bar);
    });
  }
  
  // Données de l'histogramme (remplacez ceci par vos données réelles)
  const histogramData = [100, 150, 80, 200, 120];
  
  // Appeler la fonction pour dessiner l'histogramme avec les données fournies
  drawHistogram(histogramData);
  