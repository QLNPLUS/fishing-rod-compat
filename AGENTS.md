# Fishing Rod Compat Working Rules

## Branch Matrix

| Branch | Worktree | Loader | Minecraft | JDK | Gradle | Build plugin |
|---|---|---|---|---:|---|---|
| `main` | `D:\projects\fishing_rod_compat-neoforge-1.21.1` | NeoForge | 1.21.1 | 21 | 8.8 | ModDevGradle 2.0.141 |
| `codex/forge-1.20.1` | `D:\projects\fishing_rod_compat-forge-1.20.1` | Forge | 1.20.1 | 17 | 8.8 | ForgeGradle 6.x |

`main` is the current GitHub default branch and the primary NeoForge worktree. The Forge worktree is a linked worktree. Each branch has its own loader-specific build and local dependency artifacts.

## Change Propagation

- Develop and verify a change on one branch first. Propagate cross-version changes only with `git cherry-pick -x`; never hand-rewrite the same change on the target branch.
- User confirmation that the tested version works, or an explicit request to sync versions, triggers propagation.
- Classify each target as applicable, not applicable because of a platform gap, or requiring API adaptation, and report that decision.
- Bug fixes found on a non-primary branch must be cherry-picked back to `main` before moving onward.

## Known Platform Gaps

- Forge and NeoForge use different loader APIs, registry packages, event buses, and remapped Tide method names.
- The two branches use different Tide, Aquaculture, Cloth Config, and Stardew Fishing artifact filenames.
- These platform-specific differences remain in their respective branches; do not collapse them by manual copying.

## Release

- New tags use `v<version>-<loader>-<minecraft-version>`, for example `v1.0-forge-1.20.1` and `v1.0-neoforge-1.21.1`.
- The CurseForge workflow must build the matching tag and upload only the current changelog section and exact versioned JAR.
- CI requires `CURSEFORGE_PROJECT_ID` and `CURSEFORGE_TOKEN`; never print the token.
