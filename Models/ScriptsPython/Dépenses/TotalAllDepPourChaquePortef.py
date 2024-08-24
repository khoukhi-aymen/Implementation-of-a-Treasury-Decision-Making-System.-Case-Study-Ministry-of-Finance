from sklearn.linear_model import LinearRegression
from sklearn.model_selection import train_test_split
import numpy as np
import sys
from datetime import datetime, timedelta
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
from sklearn.metrics import r2_score
from sklearn.preprocessing import PolynomialFeatures
from sklearn.tree import DecisionTreeRegressor,plot_tree
import plotly.io as pio
import plotly.figure_factory as ff
import plotly.graph_objs as go
import plotly.express as px
from sklearn import tree


def format_predictions(test_data, y_pred):
    predictions_list = []
    for i, prediction in enumerate(y_pred):
        days = int(test_data[i][1])
        prediction_str = f"Portefeuille: 0{test_data[i][0]}, TotalMontantDepense:  {prediction}, DATE: {(datetime(1970, 1, 1) + timedelta(days=days)).strftime('%Y-%m-%d')}"
        predictions_list.append(prediction_str)
    return predictions_list

# Récupérer la chaîne passée en paramètre depuis les arguments de la ligne de commande
input_string1 = sys.argv[1]
# Récupérer la chaîne passée en paramètre depuis les arguments de la ligne de commande
input_string2 = sys.argv[2]
# Diviser la chaîne en lignes en utilisant '|' comme séparateur
lines = input_string1.split('|')

# Initialiser une liste pour stocker les lignes de la matrice
matrix_rows = []

# Initialiser une liste pour stocker les dates
dates = []

# Parcourir chaque ligne
for line in lines:
    # Diviser la ligne en colonnes en utilisant ',' comme séparateur
    columns = line.split(',')

    # Initialiser une liste pour stocker les valeurs de colonne
    column_values = []

    # Extraire les valeurs de colonne pour 'Portefeuille', 'TotalMontantDepense:' et 'DATE'
    for column in columns:
        if 'Portefeuille' in column:
            value = int(column.split(':')[1].strip())  # Extraire et convertir en entier
            column_values.append(value)
        elif 'TotalMontantDepense:' in column:
            value = int(float(column.split(':')[1].strip()))  # Convertir en entier
            column_values.append(value)
        elif 'DATE' in column:
            date_value = datetime.strptime(column.split(':')[1].strip(), "%Y-%m-%d")  # Convertir la date en objet datetime
            column_values.insert(1, (date_value - datetime(1970, 1, 1)).days)  # Insérer la date à la deuxième position
            dates.append(date_value)

    # Ajouter les valeurs de colonne à la liste des lignes de la matrice
    matrix_rows.append(column_values)

# Créer le tableau training_data à partir des lignes obtenues
training_data = np.array(matrix_rows)

# Afficher la matrice training_data
# print("Matrice training_data :")
# print(training_data)


# Séparer les étiquettes (y) et les caractéristiques (X)
X = training_data[:, :-1]  # Les premières colonnes, sauf la dernière
y = training_data[:, -1]   # La dernière colonne

#diviser le dataset
#X_train, X_test, Y_train, Y_test = train_test_split(X, y, test_size=0.2, random_state=42)


# Créer un tableau vide pour stocker les nouvelles données
test_data = []
# Parcourir la matrice training_data
for row in training_data:
    # Extraire les valeurs de la première colonne de training_data (Portefeuille) et la deuxième colonne (date)
    Portefeuille = row[0] 
    if input_string2 == "Prevision_annuelle":
        date = row[1] + 366
    else:
        date = row[1] + 31

    # Ajouter ces valeurs à test_data
    test_data.append([Portefeuille, date])



# Convertir test_data en tableau numpy
test_data = np.array(test_data) # Nouvelles données : Portefeuille, date qui représente le X_test

#*******************************************Regression liniére multiple*************************************************
#construire le modéle c-a-d le nuage de points autrment dit la relation entre X et y (l'apprentissage par la machine)
regressor = LinearRegression()
regressor.fit(X,y)

# Faire des prédictions
y_pred = regressor.predict(test_data)

r2 = r2_score(y,y_pred)

# print("R^2 score for Multiple Linear Regression:",r2)

#*******************************************Regression liniére multiple*************************************************


#*******************************************regression poylynomiale**************************************
#créer et entrainer un modéle de regression poylynomiale
poly_reg = PolynomialFeatures(degree=2,include_bias=False)
X_poly = poly_reg.fit_transform(X)
regressor_poly = LinearRegression()
regressor_poly.fit(X_poly, y)
X_test_poly = poly_reg.transform(test_data)
y_pred_poly = regressor_poly.predict(X_test_poly)
r3 = r2_score(y, y_pred_poly)

# print("R^2 score for poylynomiale Regression:",r3)

#*******************************************regression poylynomiale**************************************

#*******************************************arbre de régression******************************************

# Créer et entraîner le modèle d'arbre de régression avec des hyperparamètres supplémentaires
regressor_tree = DecisionTreeRegressor(
    criterion='squared_error',  # critère pour mesurer la qualité d'une division
    max_depth=2,  # profondeur maximale de l'arbre
    random_state=0  # graine aléatoire pour la reproductibilité
)
regressor_tree.fit(X, y)

# Faire des prédictions sur les données de test
y_tree_pred = regressor_tree.predict(test_data)

# Calculer le score R^2 pour évaluer le modèle
r4 = r2_score(y, y_tree_pred)

# print("R^2 score for Decision Tree Regression:", r4)

#*******************************************arbre de régression******************************************

# Trouver le score R² le plus élevé
max_r2 = max(r2, r3, r4)

# Identifier le modèle avec le score le plus élevé
if max_r2 == r2:
    # Utilisation de la fonction pour formater les prédictions
    predictions_strings = format_predictions(test_data, y_pred)

    # Affichage de la liste de prédictions formatées
    for prediction_str in predictions_strings:
        print(prediction_str)
    best_model = "Linear Regression"
elif max_r2 == r3:
    # Utilisation de la fonction pour formater les prédictions
    predictions_strings = format_predictions(test_data, y_pred_poly)
    # Affichage de la liste de prédictions formatées
    for prediction_str in predictions_strings:
        print(prediction_str)
    best_model = "Polynomial Regression"
else:
    # Utilisation de la fonction pour formater les prédictions
    predictions_strings = format_predictions(test_data, y_tree_pred)
    # Affichage de la liste de prédictions formatées
    for prediction_str in predictions_strings:
        print(prediction_str)
    best_model = "arbre Regression"

# print(f"The best model is {best_model} with an R² score of {max_r2}")





# # Tracé  Régression linéaire multiple
# # Création de la surface de la régression linéaire
# x_surf, y_surf = np.meshgrid(X[:, 0], X[:, 1])
# z_surf = regressor.predict(np.c_[x_surf.ravel(), y_surf.ravel()]).reshape(x_surf.shape)

# # Création du traceur de surface 3D avec Plotly
# surface = go.Surface(x=x_surf, y=y_surf, z=z_surf, colorscale='greens')

# # Nuage de points pour les données d'entraînement
# scatter = go.Scatter3d(
#     x=X[:, 0],
#     y=X[:, 1],
#     z=y,
#     mode='markers',
#     marker=dict(color='red', size=10),
#     name='Données'
# )

# # Nuage de points pour les prédictions des données de test
# scatter_pred = go.Scatter3d(
#     x=test_data[:, 0],
#     y=test_data[:, 1],
#     z=y_pred,
#     mode='markers',
#     marker=dict(color='blue', size=10),
#     name='Prédictions'
# )

# # Création de la figure et ajout des traces
# fig = go.Figure(data=[surface, scatter, scatter_pred])

# # Mise en forme du graphique
# fig.update_layout(
#     title='Régression linéaire multiple en 3D avec Plotly',
#     scene=dict(
#         xaxis_title='Portefeuille',
#         yaxis_title='Date',
#         zaxis_title='Somme Dépenses'
#     )
# )

# # Affichage du graphique
# fig.show()


# #Tracé de la régression polynomiale
# # Plot Polynomial Regression surface
# x_surf, y_surf = np.meshgrid(np.linspace(X[:, 0].min(), X[:, 0].max(), 100), np.linspace(X[:, 1].min(), X[:, 1].max(), 100))
# X_surf_poly = poly_reg.transform(np.c_[x_surf.ravel(), y_surf.ravel()])
# z_surf = regressor_poly.predict(X_surf_poly).reshape(x_surf.shape)

# # Création du traceur de surface 3D avec Plotly
# surface = go.Surface(x=x_surf, y=y_surf, z=z_surf, colorscale='greens')

# # Nuage de points pour les données d'entraînement
# scatter = go.Scatter3d(
#     x=X[:, 0],
#     y=X[:, 1],
#     z=y,
#     mode='markers',
#     marker=dict(color='red', size=10),
#     name='Données'
# )

# # Nuage de points pour les prédictions des données de test
# scatter_pred = go.Scatter3d(
#     x=test_data[:, 0],
#     y=test_data[:, 1],
#     z=y_pred_poly,
#     mode='markers',
#     marker=dict(color='blue', size=10),
#     name='Prédictions'
# )

# # Création de la figure et ajout des traces
# fig = go.Figure(data=[surface, scatter, scatter_pred])

# # Mise en forme du graphique
# fig.update_layout(
#     title='Régression polynomiale en 3D avec Plotly',
#     scene=dict(
#         xaxis_title='Portefeuille',
#         yaxis_title='Date',
#         zaxis_title='Somme Dépenses'
#     )
# )

# # Affichage du graphique
# fig.show()



# #Tracé arbre de regression
# # Plot the tree using Matplotlib and save it as a larger image
# # Function to plot and save the tree with given figure size
# def plot_tree_with_size(width, height):
#     plt.figure(figsize=(width, height))
#     plot_tree(regressor_tree, feature_names=['Portefeuille', 'Date'], filled=True)
#     plt.show()

# # Plot with zoom-out effect (smaller figure size)
# plot_tree_with_size(15, 15)  # Zoom-out








