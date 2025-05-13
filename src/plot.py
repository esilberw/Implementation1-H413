import matplotlib.pyplot as plt
import numpy as np

# Example data
job_sizes = [50, 100]
arpd_ils = [0.859745, 1.516760535]
arpd_gls = [3.028411, ]

x = np.arange(len(job_sizes))  # position des groupes (50, 100)
width = 0.25  # largeur des barres

fig, ax = plt.subplots()
bars1 = ax.bar(x - width/2, arpd_ils, width, label='ILS', color='red')
bars2 = ax.bar(x + width/2, arpd_gls, width, label='GLS', color='green')

# Étiquettes et titre
ax.set_ylabel('ARPD (%)')
ax.set_xlabel('Number of Jobs')
ax.set_title('Average Relative Percentage Deviation (ARPD)')
ax.set_xticks(x)
ax.set_xticklabels(job_sizes)
ax.legend()

# Affichage des valeurs au-dessus des barres
for bar in bars1 + bars2:
    height = bar.get_height()
    ax.annotate(f'{height:.2f}',
                xy=(bar.get_x() + bar.get_width() / 2, height),
                xytext=(0, 3),  # décalage vertical
                textcoords="offset points",
                ha='center', va='bottom')

plt.tight_layout()
plt.savefig("arpd_comparison.png")  # Enregistre dans le fichier