import { useState } from "react";
import {
    IconButton,
    Dialog,
    DialogTitle,
    DialogContent,
    Typography,
} from "@mui/material";
import helpImg from "../assets/help.png";
import exampleImg from "../assets/example.png";

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
                        <p>Дано поле NxN клеток. Есть два игрока, которые играют белыми и черными фишками (у каждого игрока свой цвет). Все фишки одинаковы.
                        </p>
                        <p>
                        Игроки ходят по очереди, ставя фишку своего цвета в любую свободную клетку. Первый ход по договоренности. Выигрывает тот игрок, который первым выставит на поле квадрат из фишек своего цвета. Квадрат может быть ориентирован как угодно относительно доски. В случае, если все поля заполнены, но ни один игрок не смог построить квадрат, объявляется ничья. Примеры квадратов:
                        </p>
                        <img src={exampleImg} alt="help" style={{ width: 250, height: 250 }} />
                        </Typography>
                </DialogContent>
            </Dialog>
        </>
    );
}
