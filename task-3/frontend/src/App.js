import { useState } from "react";
import GameForm from "./components/GameForm";
import GameBoard from "./components/GameBoard";
import ResultModal from "./components/ResultModal";
import Help from "./components/Help";
import { makeMove } from "./api";
import "./style.css";

function App() {
    const [gameState, setGameState] = useState({
        config: null,
        board: [],
        currentPlayer: null,
        players: {},
        result: null,
        winSquare: null,
        isProcessing: false,
        showResultModal: false
    });

    const handleGameStart = async ({ size, player1Type, player1Color, player2Type, player2Color, serverData }) => {
        const newBoard = Array(size).fill(null).map(() => Array(size).fill(null));

        const players = {
            [player1Color]: { type: player1Type, color: player1Color },
            [player2Color]: { type: player2Type, color: player2Color }
        };

        let firstMoveColor = null;
        let firstMoveCoords = null;

        if (serverData.firstMove) {
            const move = parseMove(serverData.firstMove);
            if (move) {
                firstMoveColor = move.color;
                firstMoveCoords = move;
                newBoard[move.row][move.col] = move.color;
            } else if (serverData.firstMove.includes(player1Color)) {
                firstMoveColor = player1Color;
            } else if (serverData.firstMove.includes(player2Color)) {
                firstMoveColor = player2Color;
            }
        }

        let currentPlayer = null;
        if (firstMoveCoords) {
            currentPlayer = firstMoveColor === player1Color ? players[player2Color] : players[player1Color];
        } else {
            currentPlayer = players[firstMoveColor || player1Color];
        }

        const newGameState = {
            config: { size, player1Type, player1Color, player2Type, player2Color },
            board: newBoard,
            currentPlayer,
            players,
            result: null,
            winSquare: null,
            showResultModal: false,
            isProcessing: false
        };

        setGameState(newGameState);

        if (currentPlayer.type === "COMP" && serverData.nextMove) {
            const aiMove = parseMove(serverData.nextMove);
            if (aiMove) {
                setTimeout(() => performAIMove(aiMove, newGameState), 100);
            }
        }
    };

    const parseMove = (moveStr) => {
        if (!moveStr) return null;
        const match = moveStr.match(/^([BW])\s*\((\d+),(\d+)\)$/);
        if (!match) return null;
        return { color: match[1], row: parseInt(match[2], 10), col: parseInt(match[3], 10) };
    };

    const applyMoveToBoard = (move, currentBoard) => {
        if (!move) return currentBoard;
        const newBoard = currentBoard.map(r => [...r]);
        newBoard[move.row][move.col] = move.color;
        return newBoard;
    };

    const getNextPlayer = (currentPlayer, players) => {
        if (!currentPlayer || !players) return null;
        const playerColors = Object.keys(players);
        return currentPlayer.color === playerColors[0] ? players[playerColors[1]] : players[playerColors[0]];
    };

    const handleUserMove = async ({ row, col }) => {
        const { currentPlayer, isProcessing, players } = gameState;

        if (isProcessing || currentPlayer?.type !== "USER") return;

        setGameState(prev => ({ ...prev, isProcessing: true }));

        try {
            const res = await makeMove(`MOVE ${row},${col}`);

            const playerMove = parseMove(res.playerMove);
            const newBoard = applyMoveToBoard(playerMove, gameState.board);

            if (res.result) {
                setGameState(prev => ({
                    ...prev,
                    board: newBoard,
                    result: res.result,
                    winSquare: res.winSquare || null,
                    currentPlayer: null,
                    showResultModal: true,
                    isProcessing: false
                }));
                return;
            }

            const nextPlayer = getNextPlayer(currentPlayer, players);

            setGameState(prev => ({
                ...prev,
                board: newBoard,
                currentPlayer: nextPlayer,
                isProcessing: false
            }));

            if (nextPlayer?.type === "COMP" && res.nextMove) {
                const aiMove = parseMove(res.nextMove);
                if (aiMove) {
                    setTimeout(() => performAIMove(aiMove, {
                        ...gameState,
                        board: newBoard,
                        currentPlayer: nextPlayer
                    }), 200);
                }
            }
        } catch (error) {
            setGameState(prev => ({ ...prev, isProcessing: false }));
        }
    };

    const performAIMove = async (move, previousState) => {
        if (!move || gameState.isProcessing) return;

        setGameState(prev => ({ ...prev, isProcessing: true }));

        try {
            const res = await makeMove(`MOVE ${move.row},${move.col}`);

            const playerMove = parseMove(res.playerMove);
            const newBoard = applyMoveToBoard(playerMove, previousState.board);

            if (res.result) {
                setGameState(prev => ({
                    ...prev,
                    board: newBoard,
                    result: res.result,
                    winSquare: res.winSquare || null,
                    currentPlayer: null,
                    showResultModal: true,
                    isProcessing: false
                }));
                return;
            }

            const nextPlayer = getNextPlayer(previousState.currentPlayer, previousState.players);

            setGameState(prev => ({
                ...prev,
                board: newBoard,
                currentPlayer: nextPlayer,
                isProcessing: false
            }));

            if (nextPlayer?.type === "COMP" && res.nextMove) {
                const nextAIMove = parseMove(res.nextMove);
                if (nextAIMove) {
                    setTimeout(() => performAIMove(nextAIMove, {
                        ...previousState,
                        board: newBoard,
                        currentPlayer: nextPlayer
                    }), 200);
                }
            }
        } catch (error) {
            setGameState(prev => ({ ...prev, isProcessing: false }));
        }
    };

    const handleCloseModal = () => {
        setGameState(prev => ({
            ...prev,
            showResultModal: false,
            result: null
        }));
    };

    const handleNewGame = () => {
        setGameState({
            config: null,
            board: [],
            currentPlayer: null,
            players: {},
            result: null,
            winSquare: null,
            showResultModal: false,
            isProcessing: false
        });
    };

    const { config, board, currentPlayer, result, winSquare, showResultModal, isProcessing } = gameState;

    return (
        <div>
            <div style={{ position: "relative", maxWidth: 900, margin: "0 auto", height: "100vh", display: "flex", flexDirection: "column", padding: 20 }}>
                {!config ? (
                    <GameForm onGameStart={handleGameStart} />
                ) : (
                    <>
                        <GameBoard
                            size={config.size}
                            currentPlayer={currentPlayer}
                            board={board}
                            onMove={handleUserMove}
                            isProcessing={isProcessing}
                            winSquare={winSquare}
                        />
                        <button
                            onClick={handleNewGame}
                            style={{
                                position: "absolute",
                                top: 10,
                                left: 10,
                                padding: "10px 20px",
                                backgroundColor: "#1976d2",
                                color: "white",
                                border: "none",
                                borderRadius: "4px",
                                cursor: "pointer",
                                zIndex: 1000
                            }}
                        >
                            Новая игра
                        </button>
                    </>
                )}
                <ResultModal
                    open={showResultModal}
                    result={result}
                    onClose={handleCloseModal}
                />
            </div>
            <div style={{ position: "absolute", top: 10, right: 10 }}>
                <Help />
            </div>
        </div>
    );
}

export default App;