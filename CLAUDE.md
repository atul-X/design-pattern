# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

A Java learning repository with two distinct halves:
- `src/behavioural/` and `src/creational/` — classic GoF design pattern implementations, each usually paired as `problem/` (naive approach) vs `solution/` (pattern-based refactor), used to demonstrate *why* the pattern helps.
- `src/lld/` — Low-Level Design interview practice: full mini system designs (Uber, Ticketmaster, elevator, flight booking, vending machine, LRU cache, task scheduler, warehouse management, etc.), each in its own subpackage with `model/`, `service/`, `enums/`, `exception/` folders and a `*Demo.java` or `*Main.java` entry point.

There is no build tool (no Maven/Gradle) — this is plain `javac`/`java` with `src/` as the source root and packages matching directory paths (e.g. `src/behavioural/command/banking/BankAccount.java` → `package behavioural.command.banking;`).

## Common Commands

Compile everything into `bin/`:
```bash
mkdir -p bin
find src -name "*.java" -print0 | xargs -0 javac -d bin -cp src
```

Run any example by fully-qualified class name after compiling:
```bash
java -cp bin behavioural.command.WithCommondPattern
java -cp bin behavioural.memento.texteditor.TextEditorMain
java -cp bin creational.singleton.logger.Exercise
java -cp bin lld.lru.CacheService
```

To compile/run a single file in isolation (faster iteration on one pattern):
```bash
javac -d bin -cp src src/behavioural/observer/weather/*.java
java -cp bin behavioural.observer.weather.ObserverPatternExample
```

Interactive GUI showcase (Swing app tying together Singleton/Command/Observer/Iterator demos):
```bash
./run-gui.sh          # macOS/Linux: compiles everything then runs gui.DesignPatternShowcase
run-gui.bat           # Windows
```

There is no test framework or lint config in this repo. `GumballMachineTestDrive.java` (`src/behavioural/state/gumball/`) is a manual demo runner, not a unit test — verify changes by compiling and running the relevant `*Demo`/`*Main`/`*Example` class's `main` method.

## Architecture Notes

- **Package = directory path.** When adding a new file, its `package` declaration must mirror its location under `src/`.
- **Pattern packages favor a `problem/` vs `solution/` split** (e.g. `creational/abstractfactorypattern/problem` vs `.../solution`) so both the anti-pattern and the fixed version compile and run side by side — preserve this structure when adding a new pattern example rather than deleting the "problem" version.
- **LLD packages are structured like small services**, not single files: `model/` (entities), `service/` (business logic), `enums/`, `exception/` (custom exceptions), with a top-level `*Demo.java` wiring them together. Follow this layout for new LLD problems rather than putting everything in one file.
- Each pattern/LLD example is self-contained with its own `main`/demo entry point — there is no shared `Main.java` orchestrator at `src/` root.
- `docs/` contains extended write-ups (`docs/README.md` is the long-form pattern reference with code walkthroughs; `docs/hld/` has high-level design docs like rate limiter and weather reporting system; `docs/pdf/` and the root `Design+Patterns+Notes+-+Complete.pdf` are reference material, not something to regenerate).
- `HLD/` is a separate, minimal high-level-design sketch area (currently just a placeholder `Main.java` and a weather system doc) — distinct from `src/lld/`.
- `out/` and `bin/` are compiler output directories (gitignored) — never hand-edit files there.
