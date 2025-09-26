import { useState } from "react";
import {
    Box,
    Button,
    TextField,
    Typography,
    RadioGroup,
    FormControlLabel,
    Radio,
} from "@mui/material";
import { startGame } from "../api";

export default function GameForm({ onGameStart }) {
    const [size, setSize] = useState(8);
    const [player1Type, setPlayer1Type] = useState("USER");
    const [player1Color, setPlayer1Color] = useState("B");
    const [player2Type, setPlayer2Type] = useState("COMP");
    const [player2Color, setPlayer2Color] = useState("W");

    const handleColorChange = (player, color) => {
        if (player === 1) {
            setPlayer1Color(color);
            setPlayer2Color(color === "B" ? "W" : "B");
        } else {
            setPlayer2Color(color);
            setPlayer1Color(color === "B" ? "W" : "B");
        }
    };

    const handleStart = async () => {
        const gameConfig = `GAME ${size} ${player1Type} ${player1Color} ${player2Type} ${player2Color}`;
        const res = await startGame(gameConfig);
        onGameStart({ size, player1Type, player1Color, player2Type, player2Color, serverData: res });
    };

    return (
        <Box sx={{ p: 3, border: "1px solid gray", borderRadius: 2, maxWidth: 400, margin: "0 auto" }}>
            <Typography variant="h6" gutterBottom>Начать игру</Typography>

            <TextField
                label="Размер игрового поля N"
                type="number"
                value={size}
                onChange={(e) => setSize(Number(e.target.value))}
                fullWidth
                sx={{ mb: 3 }}
            />

            {/* Игрок 1 */}
            <Typography>Игрок 1:</Typography>
            <RadioGroup row value={player1Type} onChange={(e) => setPlayer1Type(e.target.value)}>
                <FormControlLabel value="USER" control={<Radio />} label="Человек" />
                <FormControlLabel value="COMP" control={<Radio />} label="ИИ" />
            </RadioGroup>
            <RadioGroup
                row
                value={player1Color}
                onChange={(e) => handleColorChange(1, e.target.value)}
            >
                <FormControlLabel value="B" control={<Radio />} label="Черный" />
                <FormControlLabel value="W" control={<Radio />} label="Белый" />
            </RadioGroup>

            {/* Игрок 2 */}
            <Typography sx={{ mt: 2 }}>Игрок 2:</Typography>
            <RadioGroup row value={player2Type} onChange={(e) => setPlayer2Type(e.target.value)}>
                <FormControlLabel value="USER" control={<Radio />} label="Человек" />
                <FormControlLabel value="COMP" control={<Radio />} label="ИИ" />
            </RadioGroup>
            <RadioGroup
                row
                value={player2Color}
                onChange={(e) => handleColorChange(2, e.target.value)}
            >
                <FormControlLabel value="B" control={<Radio />} label="Черный" />
                <FormControlLabel value="W" control={<Radio />} label="Белый" />
            </RadioGroup>

            <Button
                variant="contained"
                color="primary"
                sx={{ mt: 3 }}
                fullWidth
                onClick={handleStart}
            >
                Старт
            </Button>
        </Box>
    );
}