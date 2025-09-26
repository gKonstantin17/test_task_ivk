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
        <div
            style={{
                position: "relative",
                maxWidth: 900,
                margin: "0 auto",
                height: "100vh",
                display: "flex",
                flexDirection: "column",
                padding: 20,
                boxSizing: "border-box",
            }}
        >
            {!gameConfig ? (
                <GameForm onGameStart={setGameConfig} />
            ) : (
                <div style={{ flex: 1, minHeight: 0 }}>
                    <GameBoard
                        size={gameConfig.size}
                        currentPlayer={{ type: gameConfig.player1Type, color: gameConfig.player1Color }}
                        onMove={(res) => setResult(res.cmd || res.result || "Ход сделан")}
                    />
                </div>
            )}

            <ResultModal open={!!result} result={result} onClose={() => setResult(null)} />
            <Help />
        </div>
    );
}

export default App;
