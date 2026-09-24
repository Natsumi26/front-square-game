const board = document.getElementById("game-board");
const gameId = board.dataset.gameId;
const size = Number(board.dataset.size);
const gameType = board.dataset.gameType;

console.log("Type de jeu :", gameType);

let selectedPosition = null;

board.style.gridTemplateColumns = `repeat(${size}, 80px)`;

if (gameType === "connect4") {

    for (let y = size-1; y >= 0; y--) {

        for (let x = 0; x < size; x++) {

            const cell = document.createElement("div");

            cell.classList.add("game-cell");

            cell.dataset.x = x.toString();
            cell.dataset.y = y.toString();

            cell.addEventListener("click", async () => {

                const x = Number(cell.dataset.x);
                const y = Number(cell.dataset.y);

                console.log("Case sélectionnée :", x, y);

                selectedPosition = {
                    x: x,
                    y: y
                };

                const possibleMoves = await getPossibleMoves();

                console.log("Coups possibles :", possibleMoves);

                highlightPossibleMoves(possibleMoves);
            });

            board.appendChild(cell);
        }
    }

} else if(gameType === "tictactoe") {
    // Création des cases Tictactoe
    for (let y = 0; y < size; y++) {

        for (let x = 0; x < size; x++) {

            const cell = document.createElement("div");

            cell.classList.add("game-cell");

            cell.dataset.x = x;
            cell.dataset.y = y;

            cell.addEventListener("click", async () => {

                const x = Number(cell.dataset.x);
                const y = Number(cell.dataset.y);

                console.log("Case sélectionnée :", x, y);

                selectedPosition = {
                    x: x,
                    y: y
                };

                const possibleMoves = await getPossibleMoves();

                console.log("Coups possibles :", possibleMoves);

                highlightPossibleMoves(possibleMoves);
            });
            board.appendChild(cell);

        }
    }

} else if(gameType === "15 puzzle") {
    // Création des cases Tictactoe
    for (let y = 0; y < size; y++) {

        for (let x = 0; x < size; x++) {

            const cell = document.createElement("div");

            cell.classList.add("game-cell");

            cell.dataset.x = x;
            cell.dataset.y = y;

            cell.addEventListener("click", async () => {

                const x = Number(cell.dataset.x);
                const y = Number(cell.dataset.y);

                console.log("Case sélectionnée :", x, y);

                selectedPosition = {
                    x: x,
                    y: y
                };

                const possibleMoves = await getPossibleMovesForToken(selectedPosition.x, selectedPosition.y);

                console.log("Coups possibles :", possibleMoves);

                highlightPossibleMoves(possibleMoves);
            });
            board.appendChild(cell);

        }
    }
}

// Récupération des jetons
const tokens = board.querySelectorAll(".game-token");

tokens.forEach(token => {

    const x = token.dataset.x;
    const y = token.dataset.y;

    const cell = board.querySelector(
        `.game-cell[data-x="${x}"][data-y="${y}"]`
    );

    if (cell) {
        cell.textContent = token.textContent;
    }

    token.remove();
});

async function getPossibleMoves() {

    const response = await fetch(
        `/games/${gameId}/possiblemoves`
    );
    console.log(response)
    if (!response.ok) {
        console.error("Impossible de récupérer les coups possibles");
        return [];
    }

    return await response.json();
}

async function getPossibleMovesForToken(x, y) {

    const response = await fetch(
        `/games/${gameId}/tokens/${x}/${y}/possiblemoves`
    );
    console.log(response)
    if (!response.ok) {
        console.error(
            "Impossible de récupérer les coups possibles"
        );
        return [];
    }

    return await response.json();
}

function highlightPossibleMoves(possibleMoves) {

    possibleMoves.forEach(move => {

        // =========================
        // CONNECT FOUR
        // =========================
        if ((gameType === "connect4" && move.y === -1) || (gameType === "tictactoe")) {

            const cells = board.querySelectorAll(
                `.game-cell[data-x="${move.x}"]`
            );

            cells.forEach(cell => {
                cell.classList.add("possible-move");

                cell.addEventListener("click", async () => {

                    const to = {
                        x: move.x,
                        y: move.y
                    };

                    console.log("Coup Connect Four :", {
                        from: null,
                        to: to
                    });

                    await playMove(null, to);
                }, { once: true });
            });

            return;
        }


        // =========================
        // TAQUIN
        // =========================
        const cell = board.querySelector(
            `.game-cell[data-x="${move.x}"][data-y="${move.y}"]`
        );

        if (cell) {
            cell.classList.add("possible-move");

            const from = {
                x: selectedPosition.x,
                y: selectedPosition.y
            }

            cell.addEventListener("click", async (event) => {
                event.stopPropagation();

                const to = {
                    x: move.x,
                    y: move.y
                };

                console.log("Coup joué :", {
                    from: from,
                    to: to
                });

                await playMove(from, to);
            }, { once: true });
        }
    });
}

async function playMove(from, to) {

    const response = await fetch(`/games/${gameId}/moves`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            from: from,
            to: to
        })
    });

    if (!response.ok) {

        if (response.status === 403) {
            alert("Ce n'est pas votre tour !");
        } else {
            alert("Impossible de jouer ce coup.");
        }

        return false;
    }

    console.log("Coup joué !");
    window.location.reload();
    return true;
}
/*Popup de victoire*/

const victoryPopup = document.getElementById("victory-popup");

if (victoryPopup) {

    const confettiContainer =
        document.getElementById("confetti-container");

    for (let i = 0; i < 100; i++) {

        const confetti = document.createElement("div");

        confetti.classList.add("confetti");

        confetti.style.left = Math.random() * 100 + "%";
        confetti.style.animationDelay =
            Math.random() * 2 + "s";

        confettiContainer.appendChild(confetti);
    }

    const closeButton =
        document.getElementById("close-victory");

    closeButton.addEventListener("click", () => {
        victoryPopup.remove();
    });
}
