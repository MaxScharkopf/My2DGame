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

**Pending remote task:** On GitHub, change the default branch from `master` to `main` in repo Settings → Branches, then run:
```
git push origin --delete master
```

---

## Codebase Map

```
src/
  main/         # Core systems — GamePanel, Main, KeyHandler, UI, Sound, Config,
                #   CollisionChecker, AssetSetter, EventHandler, UtilityTool
  entity/       # Entity base class + Player, NPCs, Projectile, Particle, interfaces
  objects/      # All item/object classes (OBJ_*)
  monster/      # Monster classes (MON_*)
  critters/     # Critter classes (CRIT_*)
  tile/         # Tile, TileManager
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

### Phase 1 — Foundation (Interfaces & Enums)
- [ ] **`feature/entity-type-enum`** — Replace `type_*` int constants with `EntityType` enum
- [ ] **`feature/gamestate-enum`** — Replace `gameState` int constants with `GameState` enum
- [ ] **`feature/interfaces-core`** — Flesh out meaningful interfaces:
  - `IUpdatable` → `update()`
  - `IDrawable` → `draw(Graphics2D g2)`
  - `ICollidable` → `solidArea`, `collisionOn`
  - `ILiving` → `life`, `maxLife`, `alive`, `dying`, `takeDamage(int)`
  - `ICombatant` → `attack`, `defense`, `damageReaction()`
  - `IInventory` → `inventory`, `pickUp()`, `use()`
  - `IProjectile` (already exists, needs expansion)

### Phase 2 — Package Restructure
- [ ] **`feature/package-restructure`** — Move classes to proper packages:
  - `engine/` → GamePanel, Main (keep minimal)
  - `input/` → KeyHandler
  - `audio/` → Sound
  - `ui/` → UI
  - `config/` → Config
  - `physics/` → CollisionChecker
  - `world/` → AssetSetter, EventHandler, EventRect, TileManager

### Phase 3 — Entity Hierarchy
- [ ] **`feature/entity-split`** — Split `Entity` into role-specific abstract classes:
  - `LivingEntity extends Entity` — for things that have HP (player, monster, npc)
  - `ItemEntity extends Entity` — for objects/items
  - `ProjectileEntity extends Entity` — for projectiles
- [ ] **`feature/gamepanel-slim`** — Extract entity management out of GamePanel into dedicated managers (e.g. `EntityManager`, `ObjectManager`)

### Phase 4 — Systems
- [ ] **`feature/combat-system`** — Centralize combat logic (damage calculation) into a `CombatSystem` class instead of spreading it across Player, Entity, and Monster
- [ ] **`feature/game-loop-refactor`** — Clean up the game loop, possibly extract update and render phases

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
| 2026-06-28 | Initial PRP created. Baseline committed. Branch structure (main/dev) set up. Remote `master` rename pending GitHub settings change. |

> **Update this table at the end of every session.**
