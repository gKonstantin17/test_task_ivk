export async function startGame(config) {
    const response = await fetch("http://localhost:8281/start-game", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ cmd: config }),
    });
    return response.json();
}

export async function makeMove(move) {
    const response = await fetch("http://localhost:8281/move", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ cmd: move }),
    });
    return response.json();
}