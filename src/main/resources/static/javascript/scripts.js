// scripts.js

document.addEventListener("DOMContentLoaded", function() {
    // Log lorsque la page est complètement chargée
    console.log("Bienvenue sur le site des Jeux Olympiques Paris 2024 !");

    // Sélectionne tous les liens dans la barre de navigation pour des animations d'interaction
    const navLinks = document.querySelectorAll('nav ul li a');
    navLinks.forEach(link => {
        link.addEventListener('mouseenter', function() {
            link.style.transform = "scale(1.1)";
        });

        link.addEventListener('mouseleave', function() {
            link.style.transform = "scale(1)";
        });
    });

    // Gestion de la redirection vers la page de connexion avec une transition plus douce
    const loginLink = document.querySelector('a[href="/login"]');
    if (loginLink) {
        loginLink.addEventListener('click', function(event) {
            event.preventDefault(); // Empêche la navigation immédiate
            // Ajoute une animation visuelle pour la redirection
            const body = document.querySelector('body');
            body.style.transition = "opacity 0.5s ease";
            body.style.opacity = "0"; // Transition douce pour l'effet de sortie

            // Attends 500ms avant la redirection pour laisser l'animation se terminer
            setTimeout(() => {
                window.location.href = "/login";
            }, 500);
        });
    }

    // Effet visuel sur le bouton de paiement pour attirer l'attention
    const paymentButton = document.querySelector('button');
    if (paymentButton) {
        paymentButton.addEventListener('mouseenter', function() {
            paymentButton.style.backgroundColor = "#ffcc00"; // Change la couleur pour le jaune JO
            paymentButton.style.color = "#000"; // Change la couleur du texte en noir
        });

        paymentButton.addEventListener('mouseleave', function() {
            paymentButton.style.backgroundColor = "#2ecc71"; // Rétablit la couleur d'origine
            paymentButton.style.color = "#fff"; // Rétablit le texte en blanc
        });
    }

    // Redirection vers la page de paiement avec effet
    const proceedPaymentLink = document.querySelector('a[href="/proceed-to-payment"]');
    if (proceedPaymentLink) {
        proceedPaymentLink.addEventListener('click', function(event) {
            event.preventDefault();
            // Même animation de transition que pour le lien de connexion
            const body = document.querySelector('body');
            body.style.transition = "opacity 0.5s ease";
            body.style.opacity = "0"; // Transition douce pour l'effet de sortie

            // Redirection après l'animation
            setTimeout(() => {
                window.location.href = "/proceed-to-payment";
            }, 500);
        });
    }

    // Ajout d'une animation douce pour l'apparition des contenus à chaque section
    const fadeElements = document.querySelectorAll('.fade-in');
    fadeElements.forEach((element) => {
        element.style.opacity = 0;
        element.style.transition = "opacity 1s ease";

        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.style.opacity = 1;
                }
            });
        });

        observer.observe(element);
    });
});
