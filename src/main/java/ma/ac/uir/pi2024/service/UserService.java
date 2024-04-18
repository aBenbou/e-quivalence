package ma.ac.uir.pi2024.service;

import ma.ac.uir.pi2024.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    // Supposons que nous avons une liste statique pour simuler une base de données
    private static List<User> users = new ArrayList<>();

    public static boolean authenticate(String username, String password) {
        return false;
    }

    // Méthode pour enregistrer un utilisateur dans la liste (simulant l'enregistrement dans une base de données)
    public static void saveUser(User user) {
        users.add(user);
    }

    // Méthode pour vérifier si un utilisateur avec l'email donné existe déjà dans la liste
    public static boolean existsByEmail(String email) {
        // Parcourir la liste des utilisateurs et vérifier si l'email est déjà utilisé
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return true; // L'email est déjà utilisé, donc l'utilisateur existe déjà
            }
        }
        return false; // Aucun utilisateur avec cet email n'a été trouvé
    }

    // Méthode pour valider les données utilisateur
    public boolean isValidUserData(User user) {
        // Vérifier si les champs obligatoires sont renseignés
        if (user.getNom() == null || user.getPrenom() == null || user.getDateNaissance() == null ||
                user.getNumIdentite() == null || user.getNumMassar() == null || user.getEmail() == null || user.getAdresse() == null) {
            return false;
        }

        // Vérifier si les formats des données sont corrects (exemple: validation d'email)
        if (!isValidEmail(user.getEmail())) {
            return false;
        }

        // Si toutes les validations réussissent, retournez true
        return true;
    }

    // Méthode pour vérifier l'unicité des données utilisateur
    public boolean isUserDataUnique(User user) {
        // Vérifier si l'email est déjà utilisé par un autre utilisateur
        if (existsByEmail(user.getEmail())) {
            return false; // L'email est déjà utilisé, donc les données ne sont pas uniques
        }

        // Si l'email est unique, retournez true
        return true;
    }
    private boolean isValidEmail(String email) {
        // Implémentez votre logique de validation d'email ici
        // Pour cet exemple, vérifions simplement si l'email contient un "@" et un "."
        return email != null && email.contains("@") && email.contains(".");
    }
}

