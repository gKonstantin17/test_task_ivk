import { useState } from "react";
import GameForm from "./components/GameForm";
import GameBoard from "./components/GameBoard";
import ResultModal from "./components/ResultModal";
import Help from "./components/Help";
import "./style.css";

function App() {
    const [gameConfig, setGameConfig] = useState(null);
    const [result, setResult] = useState(null);

    return (
        <div style={{ position: "relative", padding: 20, maxWidth: 900, margin: "0 auto" }}>
            {!gameConfig ? (
                <GameForm onGameStart={setGameConfig} />
            ) : (
                <GameBoard
                    size={gameConfig.size}
                    currentPlayer={{ type: gameConfig.player1Type, color: gameConfig.player1Color }}
                    onMove={(res) => setResult(res.cmd || res.result || "Ход сделан")}
                />
            )}

            <ResultModal open={!!result} result={result} onClose={() => setResult(null)} />
            <Help />
        </div>
    );
}

export default App;
