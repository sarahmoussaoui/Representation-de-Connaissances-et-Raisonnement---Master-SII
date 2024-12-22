import os


def lire_fichier(nom_fichier):
    try:
        with open(nom_fichier, 'r') as fichier:
            contenu = fichier.readlines()
        return contenu
    except FileNotFoundError:
        print("Erreur: Le fichier spécifié n'existe pas.")
        return None


def ecrire_fichier(nom_fichier, contenu):
    with open(nom_fichier, 'w') as fichier:
        for ligne in contenu:
            fichier.write(ligne)


def creer_formule():
    n_lit = 1
    formule = []
    print("1: Na;\t\t 2: Nb;\t\t 3: Nc;\t \n 4: Cea;\t 5: Ceb:\t 6: Cec;\t \n 7: Coa;\t 8: Cob;\t 9: Coc;\t \n 10: Ma;\t 11: Mb;\t 12: Mc;\t\n\n")
    # print("1: K1;\t\t 2: K2;\t\t 3: K3;\t \n 4: K4;\t \t 5: K5:\t\t 6: K6;\t \n 7: K7;\t\t 8: K8;\t\t 9: K9;\t \n 10: K10;\t 11: K11;\t 12: K12;\t \n 13: K13;\t 14: K14;\t 15: K15;\t \n 16: K16;\t 17: K17;\t 18: K18;\t \n 19: K19;\t 20: K20;\n\n")

    for i in range(n_lit):
        lit = input(f"Entrez le littéral {i+1} (précédé de '-' si négatif): ")
        # Vérifier si le littéral commence par '-'
        if lit.startswith('-'):
            # Si le littéral commence par '-', retirer le '-' pour obtenir la forme sans négation
            lit = lit[1:]
        else:
            # Si le littéral ne commence pas par '-', ajouter '-' pour obtenir la forme négative
            lit = '-' + lit
        formule.append(lit)
    return formule


def generer_fichier_cnf(base_connaissances, formule):
    # formule_neg = ['-' + lit for lit in formule]
    # formule_concat = [lit for lit in formule]
    if len(formule) > 0:
        formule.append('0')

    formule_concat = ' '.join(formule)
    # print(base_connaissances)
    # print(formule_concat)
    length = len(base_connaissances)
    cnf = base_connaissances.append(formule_concat + '\n')

    # Split the first line into individual elements
    first_line = base_connaissances[0].split()
    befor_last = base_connaissances[length-1]
    # print(befor_last)
    base_connaissances[length-1] = befor_last + '\n'
    # Extract the number of clauses and convert it to an integer
    num_clauses = int(first_line[3])
    num_clauses += 1  # Increment the number of clauses by one
    # Convert the incremented number of clauses back to a string
    first_line[3] = str(num_clauses)

    # Join the modified elements back into a single string
    base_connaissances[0] = ' '.join(first_line) + '\n'

    # print(base_connaissances)
    # cnf[1] = f"{int(cnf[1]) + 1}\n"
    return base_connaissances


def tester_inference(nom_fichier, formule):
    fichier_cnf = "temp.cnf"
    fichier_resultat = "resultat.txt"

    cnf = generer_fichier_cnf(lire_fichier(nom_fichier), formule)

    # print(cnf)

    ecrire_fichier(fichier_cnf, cnf)

    os.system(f"ubcsat -alg saps -i {fichier_cnf} -solve > {fichier_resultat}")

    with open(fichier_resultat, 'r') as resultat:
        if "# Solution found for -target 0" in resultat.read():
            print("La base de connaissances infère la formule.")
        else:
            print("La base de connaissances n'infère pas la formule.")


def main():
    nom_fichier = input(
        "Entrez le nom du fichier contenant la base de connaissances: ")
    while lire_fichier(nom_fichier) is None:
        nom_fichier = input("Entrez un nom de fichier valide: ")

    formule = creer_formule()
    tester_inference(nom_fichier, formule)


if __name__ == "__main__":
    main()
