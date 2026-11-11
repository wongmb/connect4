export type GamePlayer = 'R' | 'Y'

export type GameCell = GamePlayer | '.'

export type GameStatus =
  | 'IN_PROGRESS'
  | 'DRAW'
  | 'RED_WON'
  | 'YELLOW_WON'

export interface Game {
  id: number
  board: string
  currentPlayer: GamePlayer
  status: GameStatus
}

export interface MoveRequest {
  column: number
}
