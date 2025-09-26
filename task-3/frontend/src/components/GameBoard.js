import { useState } from "react";
import { Box } from "@mui/material";
import { makeMove } from "../api";

export default function GameBoard({ size, currentPlayer, onMove }) {
    const [selected, setSelected] = useState(null); // {row, col}
    const [board, setBoard] = useState(Array(size).fill(null).map(() => Array(size).fill(null)));

    const handleClick = (row, col) => {
        if (!selected) {
            setSelected({ row, col });
        } else {
            // подтверждение хода
            const moveStr = `MOVE ${row},${col}`;
            makeMove(moveStr).then((result) => {
                setBoard((prev) => {
                    const newBoard = prev.map((r) => [...r]);
                    newBoard[row][col] = currentPlayer.color;
                    return newBoard;
                });
                onMove(result);
            });
            setSelected(null);
        }
    };

    return (
        <Box
            sx={{
                display: "grid",
                gridTemplateColumns: `repeat(${size}, 40px)`,
                gap: "2px",
                mt: 2,
            }}
        >
            {board.map((row, rIdx) =>
                row.map((cell, cIdx) => (
                    <Box
                        key={`${rIdx}-${cIdx}`}
                        onClick={() => handleClick(rIdx, cIdx)}
                        sx={{
                            width: 40,
                            height: 40,
                            border: "1px solid black",
                            backgroundColor:
                                selected?.row === rIdx && selected?.col === cIdx
                                    ? "yellow"
                                    : cell === "B"
                                        ? "black"
                                        : cell === "W"
                                            ? "white"
                                            : "green",
                            cursor: "pointer",
                        }}
                    />
                ))
            )}
        </Box>
    );
}