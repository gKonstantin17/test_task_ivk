import { Dialog, DialogTitle, DialogContent, DialogActions, Button, Typography } from "@mui/material";

export default function ResultModal({ open, result, onClose }) {
    return (
        <Dialog open={open} onClose={onClose}>
            <DialogTitle>Результат игры</DialogTitle>
            <DialogContent>
                <Typography>{result}</Typography>
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose}>Ок</Button>
            </DialogActions>
        </Dialog>
)
}