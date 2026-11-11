import { useEffect, useRef, useState } from 'react'
import { createGame, makeMove } from './api/games'
import GameBoard from './components/GameBoard'
import type { Game } from './types/game'
import './App.css'

type RequestState = 'creating' | 'moving' | null

function App() {
  const [game, setGame] = useState<Game | null>(null)
  const [requestState, setRequestState] = useState<RequestState>('creating')
  const [error, setError] = useState<string | null>(null)
  const initialGameRequest = useRef<Promise<Game> | null>(null)
  const requestInProgress = useRef(false)

  useEffect(() => {
    let active = true
    initialGameRequest.current ??= createGame()

    initialGameRequest.current
      .then((createdGame) => {
        if (active) {
          setGame(createdGame)
          setError(null)
        }
      })
      .catch((requestError: unknown) => {
        if (active) {
          setError(
            requestError instanceof Error
              ? requestError.message
              : 'Unable to create a game.',
          )
        }
      })
      .finally(() => {
        if (active) {
          setRequestState(null)
        }
      })

    return () => {
      active = false
    }
  }, [])

  async function handleNewGame() {
    if (requestInProgress.current) return

    requestInProgress.current = true
    setRequestState('creating')
    setError(null)

    try {
      setGame(await createGame())
    } catch (requestError) {
      setError(
        requestError instanceof Error
          ? requestError.message
          : 'Unable to create a game.',
      )
    } finally {
      requestInProgress.current = false
      setRequestState(null)
    }
  }

  async function handleColumnClick(column: number) {
    if (
      requestInProgress.current ||
      !game ||
      game.status !== 'IN_PROGRESS'
    ) {
      return
    }

    requestInProgress.current = true
    setRequestState('moving')
    setError(null)

    try {
      setGame(await makeMove(game.id, { column }))
    } catch (requestError) {
      setError(
        requestError instanceof Error
          ? requestError.message
          : 'Unable to make that move.',
      )
    } finally {
      requestInProgress.current = false
      setRequestState(null)
    }
  }

  const isGameOver = game !== null && game.status !== 'IN_PROGRESS'
  const boardDisabled =
    requestState !== null || !game || isGameOver

  let gameMessage = 'Starting a new game…'
  if (requestState === 'moving') {
    gameMessage = 'Updating the board…'
  } else if (game?.status === 'DRAW') {
    gameMessage = "It's a draw!"
  } else if (game?.status === 'RED_WON') {
    gameMessage = 'Red wins!'
  } else if (game?.status === 'YELLOW_WON') {
    gameMessage = 'Yellow wins!'
  } else if (game?.status === 'IN_PROGRESS') {
    gameMessage = `${game.currentPlayer === 'R' ? 'Red' : 'Yellow'}'s turn`
  } else if (error) {
    gameMessage = 'Could not load the game.'
  }

  return (
    <main className="app">
      <header className="app__header">
        <div className="app__brand" aria-hidden="true">
          <span className="app__brandmark">
            <span />
            <span />
            <span />
            <span />
          </span>
          <span className="app__eyebrow">The classic four-in-a-row</span>
        </div>
        <h1>Connect Four</h1>
        <p className="app__subtitle">
          Take turns, think ahead, and connect four to win.
        </p>
      </header>

      <section className="game" aria-label="Connect Four game">
        <div className="game__toolbar">
          <div
            className={`game__status game__status--${game?.status.toLowerCase() ?? 'loading'}`}
            aria-live="polite"
            role="status"
          >
            <span className="game__status-dot" aria-hidden="true" />
            {requestState === 'creating' && game
              ? 'Starting a new game…'
              : gameMessage}
          </div>
          <button
            className="game__new-button"
            type="button"
            onClick={handleNewGame}
            disabled={requestState !== null}
          >
            <span className="game__button-icon" aria-hidden="true">
              ↻
            </span>
            {requestState === 'creating' ? 'Starting…' : 'New game'}
          </button>
        </div>

        {game && (
          <div className="game__players" aria-label="Players">
            <span
              className={`game__player game__player--red${game.status === 'IN_PROGRESS' && game.currentPlayer === 'R' ? ' game__player--active' : ''}`}
            >
              <span
                className="game__piece game__piece--red"
                aria-hidden="true"
              />
              <span className="game__player-label">Red</span>
            </span>
            <span
              className={`game__player game__player--yellow${game.status === 'IN_PROGRESS' && game.currentPlayer === 'Y' ? ' game__player--active' : ''}`}
            >
              <span
                className="game__piece game__piece--yellow"
                aria-hidden="true"
              />
              <span className="game__player-label">Yellow</span>
            </span>
          </div>
        )}

        {game ? (
          <GameBoard
            board={game.board}
            onColumnClick={handleColumnClick}
            disabled={boardDisabled}
          />
        ) : (
          <div className="game__placeholder" aria-hidden="true">
            {requestState === 'creating' ? 'Loading board…' : 'No game started'}
          </div>
        )}

        {error && (
          <p className="game__error" role="alert">
            {error}
          </p>
        )}
      </section>
    </main>
  )
}

export default App
