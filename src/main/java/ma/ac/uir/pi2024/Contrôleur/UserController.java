package ma.ac.uir.pi2024.Contrôleur;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ma.ac.uir.pi2024.model.User;
import ma.ac.uir.pi2024.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    // Méthode pour valider les données utilisateur
    private boolean isValidUserData(User user) {
        // Vérifier si les champs obligatoires sont renseignés
        if (user.getNom() == null || user.getPrenom() == null || user.getDateNaissance() == null ||
                user.getNumIdentite() == null || user.getNumMassar() == null || user.getEmail() == null || user.getAdresse() == null) {
            return false;
        }

        // Vous pouvez ajouter d'autres règles de validation ici

        // Si toutes les validations réussissent, retournez true
        return true;
    }

    // Méthode pour vérifier l'unicité des données utilisateur
    private boolean isUserDataUnique(User user) {
        // Vérifier si l'email est déjà utilisé par un autre utilisateur
        if (UserService.existsByEmail(user.getEmail())) {
            return false; // L'email est déjà utilisé, donc les données ne sont pas uniques
        }

        // Si l'email est unique, retournez true
        return true;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
        // Supposons que vous avez une méthode dans un service d'authentification pour vérifier les informations d'identification
        if (UserService.authenticate(username, password)) {
            // Si l'authentification réussit, vous pouvez rediriger l'utilisateur vers la page d'accueil
            return "redirect:/home";
        } else {
            // Si l'authentification échoue, vous pouvez rediriger l'utilisateur vers la page de connexion avec un message d'erreur
            return "redirect:/login?error";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Récupérer la session et l'invalider
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Nettoyer les cookies d'authentification si nécessaire
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("authToken")) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    break;
                }
            }
        }

        // Rediriger vers la page de connexion
        return "redirect:/login";
    }


    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user) {
        // Valider les données d'entrée
        if (!isValidUserData(user)) {
            // Si les données ne sont pas valides, redirigez l'utilisateur vers la page d'enregistrement avec un message d'erreur
            return "redirect:/register?error";
        }

        // Vérifier l'unicité des données
        if (!isUserDataUnique(user)) {
            // Si les données ne sont pas uniques, redirigez l'utilisateur vers la page d'enregistrement avec un message d'erreur
            return "redirect:/register?error=unique";
        }

        // Sauvegarder les données dans la base de données
        try {
            UserService.saveUser(user);
            // Rediriger l'utilisateur vers la page de connexion après un enregistrement réussi
            return "redirect:/login";
        } catch (Exception e) {
            // Gérer les erreurs et les exceptions
            return "redirect:/register?error=unexpected";
        }
    }

}
