import './GameBoard.css'

interface GameBoardProps {
  board: string
  onColumnClick: (column: number) => void
  disabled?: boolean
}

function GameBoard({
  board,
  onColumnClick,
  disabled = false,
}: GameBoardProps) {
  return (
    <div
      className="game-board"
      role="group"
      aria-label="Connect Four board. Select a column to drop a piece."
    >
      {Array.from({ length: 7 }, (_, column) => (
        <button
          className="game-board__column"
          key={column}
          type="button"
          aria-label={`Drop piece in column ${column + 1}`}
          disabled={disabled}
          onClick={() => onColumnClick(column)}
        >
          {Array.from({ length: 6 }, (_, row) => {
            const cell = board[row * 7 + column]
            const color =
              cell === 'R' ? 'red' : cell === 'Y' ? 'yellow' : 'empty'

            return (
              <span
                className={`game-board__cell game-board__cell--${color}`}
                key={row}
                aria-hidden="true"
              />
            )
          })}
        </button>
      ))}
    </div>
  )
}

export default GameBoard
