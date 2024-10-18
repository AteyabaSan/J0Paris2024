// scripts.js

document.addEventListener("DOMContentLoaded", function() {
    // Affiche une alerte lorsque la page est chargée
    console.log("La page est chargée !");
    
    // Ajoute un événement au lien de connexion
    const loginLink = document.querySelector('a[href="/login"]');
    if (loginLink) {
        loginLink.addEventListener('click', function(event) {
            event.preventDefault(); // Empêche le lien de naviguer tout de suite
            alert("Redirection vers la page de connexion...");
            window.location.href = "/login"; // Redirige après l'alerte
        });
    }
});
