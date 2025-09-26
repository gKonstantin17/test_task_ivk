export async function startGame(config) {
    const response = await fetch("http://localhost:8281/start-game", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ cmd: config }),
    });
    const data = await response.json();

    // Парсим первый ход, если он есть
    if (data.firstMove) {
        const match = data.firstMove.match(/^([BW])\s*\((\d+),(\d+)\)$/);
        if (match) {
            data.firstMoveParsed = { color: match[1], row: parseInt(match[2], 10), col: parseInt(match[3], 10) };
        }
    }

    return data;
}

export async function makeMove(move) {
    const response = await fetch("http://localhost:8281/move", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ cmd: move }),
    });
    return response.json();
}