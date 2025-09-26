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
        isProcessing: false
    });

    const handleGameStart = async ({ size, player1Type, player1Color, player2Type, player2Color, serverData }) => {
        const newBoard = Array(size).fill(null).map(() => Array(size).fill(null));

        // Создаем четкую структуру игроков
        const players = {
            [player1Color]: { type: player1Type, color: player1Color },
            [player2Color]: { type: player2Type, color: player2Color }
        };

        // Определяем, кто ходит первым
        let firstMoveColor = null;
        let firstMoveCoords = null;

        // Парсим firstMove чтобы понять, кто и куда пошел
        if (serverData.firstMove) {
            const move = parseMove(serverData.firstMove);
            if (move) {
                // Это реальный ход (COMP уже сходил)
                firstMoveColor = move.color;
                firstMoveCoords = move;
                newBoard[move.row][move.col] = move.color;
            } else if (serverData.firstMove.includes(player1Color)) {
                // Это информационное сообщение (USER ходит первым)
                firstMoveColor = player1Color;
            } else if (serverData.firstMove.includes(player2Color)) {
                firstMoveColor = player2Color;
            }
        }

        // Определяем текущего игрока
        let currentPlayer = null;
        if (firstMoveCoords) {
            // COMP уже сходил, следующий - противоположный игрок
            currentPlayer = firstMoveColor === player1Color ? players[player2Color] : players[player1Color];
        } else {
            // USER ходит первым
            currentPlayer = players[firstMoveColor || player1Color];
        }

        const newGameState = {
            config: { size, player1Type, player1Color, player2Type, player2Color },
            board: newBoard,
            currentPlayer,
            players,
            result: null,
            isProcessing: false
        };

        setGameState(newGameState);

        // Если текущий игрок - COMP, делаем его ход
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

    // Ход пользователя
    const handleUserMove = async ({ row, col }) => {
        const { currentPlayer, isProcessing, players } = gameState;

        if (isProcessing || currentPlayer?.type !== "USER") return;

        setGameState(prev => ({ ...prev, isProcessing: true }));

        try {
            const res = await makeMove(`MOVE ${row},${col}`);

            // Применяем ход игрока
            const playerMove = parseMove(res.playerMove);
            const newBoard = applyMoveToBoard(playerMove, gameState.board);

            // Если игра завершена
            if (res.result) {
                setGameState(prev => ({
                    ...prev,
                    board: newBoard,
                    result: res.result,
                    currentPlayer: null,
                    isProcessing: false
                }));
                return;
            }

            // Определяем следующего игрока
            const nextPlayer = getNextPlayer(currentPlayer, players);

            setGameState(prev => ({
                ...prev,
                board: newBoard,
                currentPlayer: nextPlayer,
                isProcessing: false
            }));

            // Если следующий игрок - COMP, и сервер предоставил ход
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

            // Применяем ход ИИ
            const playerMove = parseMove(res.playerMove);
            const newBoard = applyMoveToBoard(playerMove, previousState.board);

            // Если игра завершена
            if (res.result) {
                setGameState(prev => ({
                    ...prev,
                    board: newBoard,
                    result: res.result,
                    currentPlayer: null,
                    isProcessing: false
                }));
                return;
            }

            // Определяем следующего игрока
            const nextPlayer = getNextPlayer(previousState.currentPlayer, previousState.players);

            setGameState(prev => ({
                ...prev,
                board: newBoard,
                currentPlayer: nextPlayer,
                isProcessing: false
            }));

            // ТОЛЬКО если следующий игрок - COMP, продолжаем цепочку
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

    const { config, board, currentPlayer, result, isProcessing } = gameState;

    return (
        <div style={{ position: "relative", maxWidth: 900, margin: "0 auto", height: "100vh", display: "flex", flexDirection: "column", padding: 20 }}>
            {!config ? (
                <GameForm onGameStart={handleGameStart} />
            ) : (
                <GameBoard
                    size={config.size}
                    currentPlayer={currentPlayer}
                    board={board}
                    onMove={handleUserMove}
                    isProcessing={isProcessing}
                />
            )}
            <ResultModal open={!!result} result={result} onClose={() => setGameState(prev => ({ ...prev, result: null }))} />
            <Help />
        </div>
    );
}

export default App;