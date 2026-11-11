import type { Game, MoveRequest } from '../types/game'

const GAMES_URL = '/api/games'

async function requestGame(url: string, init?: RequestInit): Promise<Game> {
  const response = await fetch(url, init)
  if (!response.ok) {
    throw new Error(
      `Game request failed: ${response.status} ${response.statusText}`,
    )
  }

  return response.json() as Promise<Game>
}

export function createGame(): Promise<Game> {
  return requestGame(GAMES_URL, { method: 'POST' })
}

export function getGame(id: number): Promise<Game> {
  return requestGame(`${GAMES_URL}/${id}`)
}

export function makeMove(id: number, move: MoveRequest): Promise<Game> {
  return requestGame(`${GAMES_URL}/${id}/moves`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(move),
  })
}
