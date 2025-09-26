import React, { useState, useEffect, useRef } from "react";
import { Box } from "@mui/material";
import { makeMove } from "../api";
import blackImg from "../assets/black.png";
import whiteImg from "../assets/white.png";

/**
 * props:
 *  - size: number (N)
 *  - currentPlayer: { type: "USER"|"COMP", color: "B"|"W" }
 *  - onMove: function(responseObject)  // вызывается после ответа сервера на /move
 *
 * Важно: родительский контейнер должен иметь ненулевую высоту (например, задать height в App),
 * тогда поле займет всё доступное пространство внутри этого контейнера.
 */
export default function GameBoard({ size, currentPlayer, onMove }) {
    const containerRef = useRef(null);

    const [selected, setSelected] = useState(null); // {row, col} - выделенная ячейка
    const [board, setBoard] = useState(() =>
        Array(size).fill(null).map(() => Array(size).fill(null))
    );
    const [cellSize, setCellSize] = useState(40);
    const [isProcessing, setIsProcessing] = useState(false);

    useEffect(() => {
        setBoard(Array(size).fill(null).map(() => Array(size).fill(null)));
        setSelected(null);
    }, [size]);

    useEffect(() => {
        const updateSize = () => {
            const el = containerRef.current;
            if (!el) return;
            const rect = el.getBoundingClientRect();
            const availableWidth = rect.width;
            const availableHeight = rect.height;
            const marginFactor = 0.9;
            const s = Math.floor(
                Math.min((availableWidth * marginFactor) / size, (availableHeight * marginFactor) / size)
            );
            setCellSize(Math.max(16, s));

        };

        updateSize();

        if (typeof ResizeObserver !== "undefined") {
            const ro = new ResizeObserver(updateSize);
            if (containerRef.current) ro.observe(containerRef.current);
            return () => ro.disconnect();
        } else {
            window.addEventListener("resize", updateSize);
            return () => window.removeEventListener("resize", updateSize);
        }
    }, [size]);

    const handleClick = async (row, col) => {
        if (isProcessing) return;
        if (board[row][col]) {
            // если уже занята, просто игнорируем
            return;
        }

        if (!selected) {
            // первый клик — выделяем
            setSelected({ row, col });
            return;
        }

        if (selected.row === row && selected.col === col) {
            // второй клик по той же ячейке — подтверждаем ход
            const moveStr = `MOVE ${row},${col}`;
            try {
                setIsProcessing(true);
                const res = await makeMove(moveStr);
                setBoard((prev) => {
                    const newBoard = prev.map((r) => [...r]);
                    newBoard[row][col] = currentPlayer.color; // 'B' или 'W'
                    return newBoard;
                });
                 if (typeof onMove === "function") onMove(res);
            } catch (err) {
                console.error("Ошибка при makeMove:", err);
            } finally {
                setIsProcessing(false);
                setSelected(null);
            }
            return;
        }

        // если выбранная ячейка != кликнутой — просто смена выделения
        setSelected({ row, col });
    };

    // Рендер одной клетки
    const renderCell = (cell, rIdx, cIdx) => {
        const isSelected = selected && selected.row === rIdx && selected.col === cIdx;
        const occupied = cell !== null; // 'B' или 'W'
        const showPiece = occupied;
        const pieceImg = cell === "B" ? blackImg : cell === "W" ? whiteImg : null;

        return (
            <Box
                key={`${rIdx}-${cIdx}`}
                onClick={() => handleClick(rIdx, cIdx)}
                sx={{
                    width: `${cellSize}px`,
                    height: `${cellSize}px`,
                    boxSizing: "border-box",
                    border: isSelected ? "3px solid #ffeb3b" : "1px solid rgba(0,0,0,0.6)",
                    backgroundColor: occupied ? "transparent" : "#ffffff", // пустая клетка — зелёная
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    cursor: isProcessing ? "wait" : occupied ? "default" : "pointer",
                    userSelect: "none",
                    position: "relative",
                }}
            >
                {showPiece && pieceImg && (
                    <img
                        src={pieceImg}
                        alt={cell === "B" ? "black" : "white"}
                        style={{
                            width: Math.floor(cellSize * 0.78),
                            height: Math.floor(cellSize * 0.78),
                            objectFit: "contain",
                            pointerEvents: "none",
                        }}
                    />
                )}
            </Box>
        );
    };

    // Размеры сетки
    const gridWidth = cellSize * size;
    const gridHeight = cellSize * size;

    return (
        <Box
            ref={containerRef}
            sx={{
                width: "100%",
                height: "100%",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                position: "relative",
                overflow: "hidden",
                p: 1,
            }}
        >
            <Box
                sx={{
                    width: `${gridWidth}px`,
                    height: `${gridHeight}px`,
                    display: "grid",
                    gridTemplateColumns: `repeat(${size}, ${cellSize}px)`,
                    gridAutoRows: `${cellSize}px`,
                    gap: "4px",
                }}
            >
                {board.map((row, rIdx) => row.map((cell, cIdx) => renderCell(cell, rIdx, cIdx)))}
            </Box>
            {/* Можно поверх показывать loader, если isProcessing true */}
            {isProcessing && (
                <Box
                    sx={{
                        position: "absolute",
                        inset: 0,
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        backgroundColor: "rgba(255,255,255,0.35)",
                        pointerEvents: "none",
                    }}
                >
                    <div>Отправляется ход...</div>
                </Box>
            )}
        </Box>
    );
}
