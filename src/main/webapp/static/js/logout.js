function handleLogout() {
    document.cookie = `authToken=; path=/; max-age=${0}`;
    window.location.href = "../..";
}