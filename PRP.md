# Project Resume Protocol (PRP)
> Read this at the start of every new chat session. It is the single source of truth for where we are and what comes next.

---

## What This Project Is

A 2D top-down RPG written in Java (Swing / AWT). No external game engine. The game has:
- A tile-based world with multiple maps
- A player character with movement, melee attacks, and projectile attacks
- NPCs (dialogue, trading), monsters (AI pathfinding, combat), critters (passive)
- Objects (weapons, shields, potions, keys, chests, doors, coins)
- Interactive tiles (trees that can be chopped)
- A full UI: HUD, inventory screen, title screen, pause, game over, options, trade screen
- Config persistence (full-screen, etc.)
- Sound effects and music

**Entry point:** `src/main/Main.java`  
**GitHub remote:** `https://github.com/MaxScharkopf/My2DGame.git`

---

## Branch Structure

| Branch | Purpose |
|--------|---------|
| `main` | Stable, shippable code only. Only merge from `dev` when a milestone is complete. |
| `dev` | Integration branch. All feature branches merge here. This is our working branch. |
| `feature/<name>` | One branch per feature/refactor task, branched off `dev`. |

**Rules:**
- Never commit directly to `main`.
- Every feature gets its own `feature/` branch off `dev`.
- Commit often with clear messages: `type(scope): description` (e.g. `refactor(entity): extract Drawable interface`).
- Merge feature → dev via pull request or direct merge once the feature compiles and works.
- Merge dev → main only at milestones.

---

## Codebase Map

```
src/
  main/         # GamePanel, Main, GameState — core loop and entry point only
  input/        # KeyHandler
  audio/        # Sound
  ui/           # UI
  config/       # Config
  physics/      # CollisionChecker
  util/         # UtilityTool
  world/        # AssetSetter, EventHandler, EventRect, TileManager
  entity/       # Entity base class + Player, NPCs, Projectile, Particle, interfaces
  objects/      # All item/object classes (OBJ_*)
  monster/      # Monster classes (MON_*)
  critters/     # Critter classes (CRIT_*)
  tile/         # Tile (data class only — TileManager moved to world/)
  tiles_interactive/ # InteractiveTile + IT_* subclasses
  ai/           # PathFinder, Node

res/            # Art and audio assets
  player/       # Player sprite sheets
  monster/, npc/, objects/, projectile/, critters/, tiles/, tiles_interactive/
  font/         # MaruMonica bitmap font
  maps/         # Map data files
  sound/        # Sound effects and music
```

---

## Current Problems (Why We're Refactoring)

### 1. `Entity.java` is a god class
It contains fields and logic for EVERY entity type: player stats, item stats, item types, combat, animation, pathfinding, drawing, particle generation, dialogue. Everything inherits from it indiscriminately.

### 2. `IEntity` is empty
The existing `IEntity` interface has no methods — it provides no contract.

### 3. Type system uses int magic constants
```java
public final int type_player = 0;
public final int type_npc = 1;
public final int type_monster = 2;
// etc.
```
These should be an `enum`.

### 4. `GamePanel.java` is a god class
It owns the game loop, all entity arrays, all game state constants, rendering, and sound. Too much responsibility.

### 5. Game state uses magic int constants
```java
public final int titleState = 0;
public final int playState = 1;
// etc.
```
Should be a `GameState` enum.

### 6. No separation between entity roles
Player, NPC, Monster, Object all extend `Entity` and inherit fields they don't need (e.g. objects have `maxMana`, monsters have `inventory`).

### 7. `main` package holds unrelated classes
`UI`, `Sound`, `Config`, `CollisionChecker`, `AssetSetter`, `EventHandler`, `KeyHandler` are all dumped in `main`.

---

## Refactor Roadmap

These are the planned features/refactors in priority order. Each one gets its own `feature/` branch.

### Phase 1 — Foundation (Interfaces & Enums) ✅ COMPLETE — merged to main 2026-06-28
- [x] **`feature/entity-type-enum`** — `EntityType` enum replaces 9 `type_*` int constants
- [x] **`feature/gamestate-enum`** — `GameState` enum replaces 9 `*State` int constants
- [x] **`feature/interfaces-core`** — Core interfaces defined and wired to `Entity`:
  - `IUpdatable`, `IDrawable`, `ILiving`, `ICombatant`, `IUsable` (new)
  - `IEntity` now extends `IUpdatable + IDrawable` (was empty)
  - `IProjectile` expanded with `haveResource()`, extends `IUpdatable`
  - All interfaces have Javadoc on the type and each method

### Phase 2 — Package Restructure ✅ COMPLETE — merged to dev 2026-06-28
- [x] **`feature/package-restructure`** — Classes moved out of `main` into dedicated packages:
  - `input/` ← KeyHandler
  - `audio/` ← Sound
  - `ui/` ← UI
  - `config/` ← Config
  - `physics/` ← CollisionChecker
  - `util/` ← UtilityTool
  - `world/` ← AssetSetter, EventHandler, EventRect, TileManager (from `tile/`)
  - `GamePanel`, `Main`, `GameState` remain in `main`
  - Entity.java: `import main.UtilityTool` → `import util.UtilityTool`
  - Player.java: `import main.KeyHandler` → `import input.KeyHandler`
  - GamePanel.java: added all new package imports

### Phase 3 — Entity Hierarchy ✅ COMPLETE — merged to dev 2026-06-28
- [x] **`feature/entity-split`** — Split `Entity` into role-specific abstract classes:
  - `LivingEntity extends Entity` — HP, stats, movement, combat, dialogue, inventory
  - `ItemEntity extends Entity` — attackValue, defenseValue, price, value
  - `Projectile extends LivingEntity` (projectiles carry useCost, extend LivingEntity)
- [x] **`feature/gamepanel-slim`** — Extracted entity arrays into dedicated managers:
  - `EntityManager`: owns npc[][], monster[][], critter[][], iTile[][], particleList, projectileList; handles all update loops
  - `ObjectManager`: owns obj[][]
  - GamePanel.update() slimmed from ~65 lines to 3; accessed via gp.em and gp.om

### Phase 4 — Systems ✅ COMPLETE — merged to dev 2026-06-28
- [x] **`feature/combat-system`** — CombatSystem centralizes hitMonster/hitPlayer/hitCritter and calcDamage; Player, LivingEntity, Projectile delegate to gp.combat
- [x] **`feature/game-loop-refactor`** — Renderer class owns tempScreen, g2, entity sort/draw loop, drawToScreen; GamePanel.run() calls renderer.draw() + renderer.drawToScreen()

### Phase 5 — UI Split & Persistence (in progress)
- [ ] **`feature/phase-5` commit 1: UI split** — Extract self-contained screen classes from `UI.java`:
  - `HUD` — owns heart/crystal images, message list, `drawPlayerLife`, `drawMessage`
  - `TitleScreen` — owns `titleScreenState`, both title screen states
  - `GameOverScreen` — owns game-over draw
  - `UI` becomes coordinator: routes to screen classes, keeps shared state (commandNum, subState, etc.)
- [ ] **`feature/phase-5` commit 2: Save/Load** — Implement `SaveData` in `config/`:
  - Saves: map, player position, stats, inventory, equipped items to `save.properties`
  - Loads: restores all of the above, re-runs AssetSetter, starts music
  - Auto-save on every map transition; LOAD GAME on title screen hooks into `SaveData.load()`
- [ ] **Input cleanup** _(future)_ — Decouple input from draw: `enterPressed` checks in `drawOptionsScreen` and `drawTradeScreen` should move to `KeyHandler`; draw methods should not consume input state.

---

## Working Conventions

- **Commit format:** `type(scope): short description`
  - Types: `feat`, `fix`, `refactor`, `chore`, `docs`
  - Example: `refactor(entity): introduce ILiving interface`
- **When to commit:** After every logical unit — new interface added, class renamed and compiling, feature working end-to-end.
- **When to branch:** Every task in the roadmap above gets its own `feature/` branch.
- **When to merge to dev:** When the feature compiles cleanly and the game still runs.
- **When to merge to main:** At the end of each Phase above.

---

## Session Log

| Date | What Was Done |
|------|--------------|
| 2026-06-28 | Initial PRP created. Baseline committed. Branch structure (main/dev) set up. Remote `master` deleted. |
| 2026-06-28 | Phase 1 complete. EntityType enum, GameState enum, and 5 core interfaces (IUpdatable, IDrawable, ILiving, ICombatant, IUsable) added. All interfaces Javadoc'd. Merged to main. |
| 2026-06-28 | Phase 2 complete. 10 classes moved from `main/` and `tile/` into dedicated packages: audio/, config/, input/, physics/, ui/, util/, world/. GamePanel, Entity, Player imports updated. Merged to dev. |
| 2026-06-28 | Phase 3 complete. Entity split into LivingEntity + ItemEntity. EntityManager/ObjectManager extracted from GamePanel. GamePanel.update() down to 3 lines. Merged to dev. |
| 2026-06-28 | Phase 4 complete. CombatSystem centralizes all damage logic. Renderer extracted render pipeline from GamePanel. GamePanel down to ~130 lines. Merged to dev. |
| 2026-06-28 | Fixed map transition freeze bug: UI.counter was shared between drawTransition() (frame timer) and drawInventory() (dead increment). Removed dead counter++ from drawInventory; changed == 50 to >= 50 in drawTransition. Merged to main. |
| 2026-06-28 | Phase 5 complete. UI split: HUD, TitleScreen, GameOverScreen extracted from UI.java (922 → ~300 lines). Save/load: SaveData in config/ persists map, position, stats, inventory to save.properties; auto-saves on map transition; LOAD GAME on title screen now works. Merged to main. |

> **Update this table at the end of every session.**
