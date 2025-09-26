import { useState } from "react";
import {
    IconButton,
    Dialog,
    DialogTitle,
    DialogContent,
    Typography,
} from "@mui/material";
import helpImg from "../assets/help.png";

export default function Help() {
    const [open, setOpen] = useState(false);

    return (
        <>
            <IconButton
                onClick={() => setOpen(true)}
                sx={{ position: "absolute", top: 10, right: 10 }}
            >
                <img src={helpImg} alt="help" style={{ width: 24, height: 24 }} />
            </IconButton>

            <Dialog open={open} onClose={() => setOpen(false)}>
                <DialogTitle>Справка</DialogTitle>
                <DialogContent>
                    <Typography variant="body2">
                        Игра начинается с выбора размера поля и параметров игроков.
                        Игроки ходят по очереди: клик по клетке выделяет её, второй клик подтверждает ход.
                        Побеждает тот, кто займет больше клеток.
                    </Typography>
                </DialogContent>
            </Dialog>
        </>
    );
}
