import React, { useState, useEffect, useRef } from "react";
import { Box } from "@mui/material";
import blackImg from "../assets/black.png";
import whiteImg from "../assets/white.png";

export default function GameBoard({ size, currentPlayer, board, onMove, winSquare, isProcessing }) {
    const containerRef = useRef(null);
    const [cellSize, setCellSize] = useState(40);
    const [boardDimensions, setBoardDimensions] = useState({ width: 0, height: 0, offsetX: 0, offsetY: 0 });

    useEffect(() => {
        const updateSize = () => {
            const el = containerRef.current;
            if (!el) return;

            const rect = el.getBoundingClientRect();
            const totalGap = (size - 1) * 2;
            const availableWidth = rect.width - totalGap;
            const availableHeight = rect.height - totalGap;

            const s = Math.floor(Math.min(availableWidth, availableHeight) / size);
            const finalCellSize = Math.max(16, s);
            setCellSize(finalCellSize);

            const boardWidth = size * finalCellSize + totalGap;
            const boardHeight = size * finalCellSize + totalGap;
            const offsetX = (rect.width - boardWidth) / 2;
            const offsetY = (rect.height - boardHeight) / 2;

            setBoardDimensions({
                width: boardWidth,
                height: boardHeight,
                offsetX,
                offsetY
            });
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

    const handleClick = (row, col) => {
        if (isProcessing || board[row][col]) return;
        onMove({ row, col });
    };

    const getCellCenter = (row, col) => {
        return {
            x: boardDimensions.offsetX + col * (cellSize + 2) + cellSize / 2,
            y: boardDimensions.offsetY + row * (cellSize + 2) + cellSize / 2,
        };
    };

    const renderWinLine = () => {
        if (!winSquare || winSquare.length < 2) return null;

        const points = winSquare.map(([r, c]) => {
            const center = getCellCenter(r, c);
            return center;
        });

        // Для polyline: добавляем первую точку в конец, чтобы замкнуть фигуру
        const polylinePoints = [...points, points[0]];
        const polylinePointsString = polylinePoints.map(p => `${p.x},${p.y}`).join(' ');

        // Для polygon: автоматически замыкает фигуру
        const polygonPointsString = points.map(p => `${p.x},${p.y}`).join(' ');

        return (
            <svg
                style={{
                    position: "absolute",
                    top: 0,
                    left: 0,
                    width: "100%",
                    height: "100%",
                    pointerEvents: "none",
                }}
            >
                {/* Вариант 1: Polygon (замкнутая фигура) */}
                <polygon
                    points={polygonPointsString}
                    fill="rgba(255,0,0,0.1)"
                    stroke="red"
                    strokeWidth={4}
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    opacity={0.8}
                />

                {/* Вариант 2: Polyline с замыканием */}
                {/* <polyline
                    points={polylinePointsString}
                    fill="none"
                    stroke="red"
                    strokeWidth={4}
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    opacity={0.8}
                /> */}

                {/* Кружки на углах */}
                {points.map((point, index) => (
                    <circle
                        key={index}
                        cx={point.x}
                        cy={point.y}
                        r={6}
                        fill="red"
                        opacity={0.8}
                    />
                ))}
            </svg>
        );
    };

    const renderCell = (cell, r, c) => {
        const pieceImg = cell === "B" ? blackImg : cell === "W" ? whiteImg : null;
        const isWinCell = winSquare?.some(([winR, winC]) => winR === r && winC === c);

        return (
            <Box
                key={`${r}-${c}`}
                onClick={() => handleClick(r, c)}
                sx={{
                    width: `${cellSize}px`,
                    height: `${cellSize}px`,
                    border: "1px solid rgba(0,0,0,0.6)",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    cursor: cell || isProcessing ? "default" : "pointer",
                    backgroundColor: isWinCell ? "rgba(255,0,0,0.2)" : "#fff",
                    transition: "background-color 0.2s",
                    '&:hover': {
                        backgroundColor: cell || isProcessing ? undefined : "rgba(0,0,0,0.05)"
                    }
                }}
            >
                {pieceImg && (
                    <img
                        src={pieceImg}
                        alt={cell}
                        style={{
                            width: cellSize * 0.78,
                            height: cellSize * 0.78,
                            filter: isWinCell ? "drop-shadow(0 0 4px red)" : "none"
                        }}
                    />
                )}
            </Box>
        );
    };

    return (
        <Box
            ref={containerRef}
            sx={{
                width: "100%",
                height: "100%",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                position: "relative"
            }}
        >
            <Box sx={{
                display: "grid",
                gridTemplateColumns: `repeat(${size}, ${cellSize}px)`,
                gridAutoRows: `${cellSize}px`,
                gap: 0.5
            }}>
                {board.map((row, r) => row.map((cell, c) => renderCell(cell, r, c)))}
            </Box>

            {winSquare && renderWinLine()}
        </Box>
    );
}