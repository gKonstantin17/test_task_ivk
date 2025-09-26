import React, { useState, useEffect, useRef } from "react";
import { Box } from "@mui/material";
import blackImg from "../assets/black.png";
import whiteImg from "../assets/white.png";

export default function GameBoard({ size, currentPlayer, board, onMove }) {
    const containerRef = useRef(null);
    const [selected, setSelected] = useState(null);
    const [cellSize, setCellSize] = useState(40);
    const [isProcessing, setIsProcessing] = useState(false);

    useEffect(() => {
        const updateSize = () => {
            const el = containerRef.current;
            if (!el) return;
            const rect = el.getBoundingClientRect();
            const s = Math.floor(Math.min(rect.width, rect.height) * 0.9 / size);
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

    const handleClick = (row, col) => {
        if (isProcessing || board[row][col]) return;
        onMove({ row, col });
    };

    const renderCell = (cell, r, c) => {
        const pieceImg = cell === "B" ? blackImg : cell === "W" ? whiteImg : null;
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
                    cursor: cell ? "default" : "pointer",
                    backgroundColor: "#fff",
                }}
            >
                {pieceImg && <img src={pieceImg} alt={cell} style={{ width: cellSize * 0.78, height: cellSize * 0.78 }} />}
            </Box>
        );
    };

    return (
        <Box ref={containerRef} sx={{ width: "100%", height: "100%", display: "flex", alignItems: "center", justifyContent: "center", position: "relative" }}>
            <Box sx={{ display: "grid", gridTemplateColumns: `repeat(${size}, ${cellSize}px)`, gridAutoRows: `${cellSize}px`, gap: 2 }}>
                {board.map((row, r) => row.map((cell, c) => renderCell(cell, r, c)))}
            </Box>
        </Box>
    );
}
