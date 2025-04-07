# Charger les bibliothèques nécessaires
install.packages("dplyr")   # Si ce n'est pas déjà installé
install.packages("ggplot2") # Pour la visualisation, si nécessaire
install.packages("stats")   # Pour les tests statistiques (déjà installé par défaut)

library(dplyr)
library(ggplot2)

# Exemple de données : deux groupes à comparer
# Remplacez ceci par vos propres données issues de vos algorithmes
set.seed(42) # Pour reproductibilité
group1 <- c(12, 15, 19, 22, 18, 25, 17, 19)  # Résultats du premier groupe (exemple)
group2 <- c(14, 16, 20, 23, 21, 26, 18, 20)  # Résultats du deuxième groupe (exemple)

# Effectuer le test de Wilcoxon
wilcox_result <- wilcox.test(group1, group2, paired = TRUE)

# Afficher les résultats du test
print(wilcox_result)

# Interprétation du test
if(wilcox_result$p.value < 0.05) {
  print("Il y a une différence statistiquement significative entre les deux groupes.")
} else {
  print("Aucune différence statistiquement significative entre les deux groupes.")
}

# Si vous souhaitez créer un boxplot pour visualiser les résultats
data <- data.frame(
  Value = c(group1, group2),
  Group = rep(c("Group1", "Group2"), each = length(group1))
)

ggplot(data, aes(x = Group, y = Value, fill = Group)) +
  geom_boxplot() +
  labs(title = "Comparaison entre deux groupes", x = "Groupes", y = "Valeurs") +
  theme_minimal()