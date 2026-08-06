# Finance Ledger Contribution and Development Workflow

This document defines the planning, branching, review, release, and packaging conventions for **Finance Ledger**.

The normal lifecycle is:

```text
Work package → Atomic issue → Branch → Commits → Pull request
→ Squash merge → Done → Milestone completion → Release
```

The standard rule is:

> One atomic issue should normally produce one pull request.

Pull requests may contain several development commits, but they are squash-merged so that `main` receives one coherent commit for each completed issue.

---

## 1. Planning model

### Project

Use the GitHub Project **Finance Ledger Development** to track issues through:

```text
Backlog → ToDo → In Progress → In Review → Done
```

The Project should primarily contain issues. Pull requests should normally appear through the issue's **Linked pull requests** field rather than as duplicate Project rows.

### Work package

A work package is a larger body of work represented by a parent issue.

Example:

```text
Implement Add Entry workflow
├── Create Add Entry popup shell
├── Create Expense entry form
├── Create Income entry form
├── Create Investment entry form
├── Add validation
└── Persist finance entries
```

A work package may span multiple milestones. It normally has no implementation branch or pull request of its own.

### Atomic issue

An atomic issue describes one independently testable and reviewable result.

Each issue should define:

- goal;
- included scope;
- acceptance criteria;
- excluded scope;
- Project metadata;
- parent issue, when applicable;
- milestone, when applicable.

### Pull request

A pull request is the proposed implementation of an issue. It should target `main`, explain the change, list validation performed, and contain:

```text
Closes #<issue-number>
```

when merging it should complete the issue.

### Milestone

A milestone represents the planned scope of a repository version.

Examples:

```text
v0.1.0 — Application Shell
v0.2.0 — Entry Forms
v0.3.0 — Domain and Persistence
```

Assign milestones to atomic issues, not both an issue and its implementing PR.

### Release

A GitHub Release is the published version associated with a Git tag.

```text
Milestone = planned version scope
Release   = published version
```

### Package

“Package” can mean three things:

1. **Work package** — a parent issue grouping atomic work.
2. **Java package** — a source-code namespace.
3. **Distribution package** — an artifact such as a JAR, RPM, or Flatpak.

End-user application artifacts should normally be attached to GitHub Releases. GitHub Packages should be reserved for reusable dependency artifacts, such as a Maven library.

---

## 2. Project fields

### Status

| Status | Meaning |
|---|---|
| `Backlog` | Accepted future work that is not ready to begin |
| `ToDo` | Scoped, prioritized, unblocked, and ready to begin |
| `In Progress` | A branch exists and implementation is active |
| `In Review` | A non-draft PR is ready for review and merge |
| `Done` | The implementation reached `main` and the issue is closed |

Rules:

- New issues default to `Backlog`.
- Move an issue to `ToDo` when it has clear scope and acceptance criteria.
- Move it to `In Progress` when implementation begins.
- A draft PR normally remains `In Progress`.
- Move it to `In Review` when the PR is marked ready.
- Use Project automation to move closed issues to `Done`.

### Type

Use one value:

| Type | Meaning |
|---|---|
| `Feature` | New capability |
| `Bug` | Correction of incorrect behaviour |
| `Refactor` | Structural improvement without intended behaviour change |
| `Documentation` | Documentation-only work |
| `Chore` | Tooling, dependencies, configuration, or maintenance |

### Priority

| Priority | Meaning |
|---|---|
| `P0` | Critical blocker, severe breakage, security concern, or data-loss risk |
| `P1` | High-priority planned work or significant defect |
| `P2` | Normal roadmap work; default for ordinary tasks |
| `P3` | Optional improvement or low-priority maintenance |

Priority describes importance, not size.

### Size

| Size | Meaning |
|---|---|
| `XS` | Very small isolated change |
| `S` | One focused task involving few files |
| `M` | Several related changes forming one deliverable |
| `L` | Large work that should usually be split into sub-issues |

### Area

Use labels or an optional `Area` field:

```text
UI
Domain
Application
Persistence
Build
Documentation
Release
```

Use `Area`, rather than `Package`, as Project metadata because one issue may affect several Java packages.

---

## 3. Issue procedure

Before writing code:

1. Check for an existing issue.
2. Identify or create the parent work package.
3. Create an atomic issue.
4. Add it to **Finance Ledger Development**.
5. Set Project fields.
6. Assign a milestone when planned for a version.
7. Move it to `ToDo` only when it is ready.

### Issue title

Use an outcome-oriented title:

```text
Create Expense entry form UI
Fix sidebar selection after opening a window
Document local development setup
```

### Issue template

```markdown
## Goal

Describe the result.

## Scope

- Included change
- Included change

## Acceptance criteria

- [ ] Observable result
- [ ] Project builds
- [ ] Relevant tests pass

## Out of scope

- Deferred work
- Unrelated work

## Relationships

Parent issue: #<number>
Milestone: <version>
```

### Metadata checklist

```text
Status:    ToDo
Type:      Feature | Bug | Refactor | Documentation | Chore
Priority:  P0 | P1 | P2 | P3
Size:      XS | S | M | L
Area:      subsystem
Assignee:  responsible developer
Milestone: target version
Parent:    parent work package
```

---

## 4. Branch nomenclature

Use:

```text
<kind>/<issue-number>-<short-kebab-case-description>
```

| Kind | Purpose |
|---|---|
| `feat` | Feature |
| `fix` | Bug fix |
| `refactor` | Internal restructuring |
| `docs` | Documentation |
| `test` | Test-only change |
| `chore` | Tooling or maintenance |
| `release` | Release preparation |
| `hotfix` | Urgent released-version fix |

Examples:

```text
feat/12-expense-entry-form
fix/18-sidebar-selection
refactor/24-extract-navigation-builder
docs/31-development-workflow
test/37-entry-validation
chore/42-add-ci-build
release/50-prepare-v0.2.0
hotfix/61-fix-ledger-load
```

Rules:

- lowercase only;
- use hyphens;
- include the issue number;
- branch from the latest `main`;
- keep branches short-lived;
- never reuse a merged branch;
- do not include unrelated work.

---

## 5. Start work locally

Verify the working tree:

```bash
git status
```

Synchronize `main`:

```bash
git switch main
git fetch origin --prune
git pull --ff-only origin main
```

Create the branch:

```bash
git switch -c feat/12-expense-entry-form
```

Verify:

```bash
git branch --show-current
git status
```

Now move the issue from `ToDo` to `In Progress`.

Optional local setting:

```bash
git config pull.ff only
```

`--ff-only` prevents an unexpected merge commit when synchronizing `main`.

---

## 6. Commit nomenclature

Use:

```text
<type>(<scope>): <imperative description>
```

Recommended types:

```text
feat fix refactor docs test chore build ci
```

Recommended scopes:

```text
ui domain application persistence build docs release
```

Examples:

```text
feat(ui): add Expense entry form shell
fix(ui): preserve sidebar selection
refactor(ui): extract sidebar row creation
docs(workflow): document branch conventions
test(domain): reject invalid monetary amounts
chore(build): update Maven compiler plugin
```

Before committing:

```bash
git status --short
git diff
git add <specific-files>
git diff --cached
git commit -m "feat(ui): add Expense entry form shell"
```

Commit rules:

- stage only issue-related files;
- review the staged diff;
- do not commit secrets or generated build output;
- avoid unrelated cleanup;
- keep commits buildable when practical.

---

## 7. Synchronize a feature branch

Before opening or updating a PR:

```bash
git fetch origin --prune
git rebase origin/main
```

Resolve conflicts, then:

```bash
git add <resolved-files>
git rebase --continue
```

First push:

```bash
git push -u origin feat/12-expense-entry-form
```

After rebasing an already-pushed branch:

```bash
git push --force-with-lease
```

Use `--force-with-lease`, not plain `--force`. Do not rewrite a shared branch without coordination.

---

## 8. Pull request procedure

Set:

```text
Base:    main
Compare: issue branch
```

PR title:

```text
feat(ui): add Expense entry form shell
```

PR body:

```markdown
Closes #12

## Summary

Explain the result and purpose.

## Changes

- Change
- Change

## Testing

- [ ] Project compiles
- [ ] Automated tests pass
- [ ] Relevant behaviour was manually verified

## Out of scope

- Deferred work
```

Status mapping:

| PR state | Issue status |
|---|---|
| No PR | `In Progress` |
| Draft PR | `In Progress` |
| Ready for review | `In Review` |
| Substantial rework required | `In Progress` |
| Merged | `Done` through issue closure |

Before merge:

- inspect every changed file;
- confirm one issue is addressed;
- verify acceptance criteria;
- compile and test;
- perform relevant manual UI checks;
- confirm package placement and naming;
- remove secrets, debug output, and generated files.

---

## 9. Merge policy

Use:

```text
Squash and merge
```

Final squash title:

```text
<type>(<scope>): <description> (#<pull-request-number>)
```

Example:

```text
feat(ui): add Add Entry popup shell (#2)
```

After merging:

1. confirm the linked issue closed;
2. confirm Project status became `Done`;
3. confirm milestone and parent progress updated;
4. delete the remote branch.

Ordinary work must not be pushed directly to `main`.

---

## 10. Synchronize after squash merge

```bash
git switch main
git fetch origin --prune
git pull --ff-only origin main
```

Verify:

```bash
git status
git log --oneline --decorate --graph -10
```

Delete the local branch after confirming the merged change exists on `main`:

```bash
git branch -D feat/12-expense-entry-form
```

A squash merge creates a new commit, so Git may not consider the original feature branch directly merged.

---

## 11. Milestone procedure

Name milestones:

```text
v<MAJOR>.<MINOR>.<PATCH> — <release name>
```

A milestone description should define:

- release goal;
- included capabilities;
- excluded capabilities;
- optional target date.

Before closing a milestone:

- required issues are closed;
- required PRs are merged;
- validation is complete;
- documentation and version metadata are updated;
- deferred issues are moved to another milestone.

Close the milestone when the corresponding release is published.

---

## 12. Versioning and release procedure

Use:

```text
MAJOR.MINOR.PATCH
```

During initial development, versions remain below `1.0.0`.

Recommended use:

- increment `MINOR` for a planned capability release;
- increment `PATCH` for compatible corrections;
- use `1.0.0` for the first intentionally stable public release;
- use pre-release suffixes for preview versions.

Examples:

```text
0.1.0
0.2.0
0.2.1
0.3.0-alpha.1
1.0.0
```

Git tags use:

```text
v0.1.0
v0.2.1
v1.0.0
```

Release preparation should itself use an issue, branch, and PR:

```text
release/50-prepare-v0.2.0
```

Publish the GitHub Release with:

```text
Tag:    v0.2.0
Target: main
Title:  Finance Ledger v0.2.0
```

Recommended release notes:

```markdown
## Highlights
## Added
## Fixed
## Changed
## Known limitations
```

Do not silently replace a published version. Publish a new version for corrections.

---

## 13. Distribution packaging

Before distributing an artifact:

1. build from the release tag or corresponding clean `main` commit;
2. match the project version to the release version;
3. run tests and startup validation;
4. document native runtime requirements;
5. generate checksums when distributing standalone files;
6. attach artifacts to the matching GitHub Release.

Possible future artifacts:

```text
finance-ledger-0.2.0.jar
finance-ledger-0.2.0.rpm
FinanceLedger-0.2.0.flatpak
```

Use GitHub Packages only for reusable dependency artifacts. Use GitHub Releases for end-user application downloads.

---

## 14. Java package convention

Root namespace:

```text
com.roycorp.financeledger
```

Recommended responsibilities:

```text
com.roycorp.financeledger
    Bootstrap and top-level composition

com.roycorp.financeledger.ui
    GTK/libadwaita windows, views, dialogs, and widgets

com.roycorp.financeledger.domain
    Pure entities, value objects, enums, and domain rules

com.roycorp.financeledger.application
    Use cases and coordination

com.roycorp.financeledger.persistence
    JSON, files, repositories, and migrations

com.roycorp.financeledger.config
    Application configuration
```

Rules:

- package names are lowercase;
- domain code must not depend on GTK;
- UI code must not implement persistence directly;
- tests mirror production packages;
- avoid dumping grounds such as `misc`, `stuff`, or an oversized `util`.

---

## 15. Recommended automation

Configure the Project to:

- auto-add repository issues;
- set new items to `Backlog`;
- move closed issues to `Done`;
- optionally archive old `Done` items.

Configure rules for `main` to:

- require pull requests;
- block force pushes;
- block branch deletion;
- require CI checks after CI is added;
- allow squash merging;
- automatically delete merged head branches.

For a solo repository, required approvals may remain zero until another regular reviewer is available.

---

## 16. Quick reference

Start:

```bash
git switch main
git fetch origin --prune
git pull --ff-only origin main
git switch -c <kind>/<issue>-<description>
```

Commit:

```bash
git status --short
git diff
git add <specific-files>
git diff --cached
git commit -m "<type>(<scope>): <description>"
```

Sync and push:

```bash
git fetch origin --prune
git rebase origin/main
git push -u origin <branch>
```

After a pushed rebase:

```bash
git push --force-with-lease
```

After squash merge:

```bash
git switch main
git fetch origin --prune
git pull --ff-only origin main
git branch -D <merged-branch>
```

Status:

```text
Backlog → ToDo → In Progress → In Review → Done
```

Relationship:

```text
Work package
└── Atomic issue
    └── Branch
        └── Pull request
            └── Squash commit on main
                └── Milestone
                    └── Release
                        └── Distribution artifact
```

---

## References

- GitHub Docs — Setting guidelines for repository contributors  
  https://docs.github.com/en/communities/setting-up-your-project-for-healthy-contributions/setting-guidelines-for-repository-contributors
- GitHub Docs — About Issues  
  https://docs.github.com/en/issues/tracking-your-work-with-issues/learning-about-issues/about-issues
- GitHub Docs — About milestones  
  https://docs.github.com/en/issues/using-labels-and-milestones-to-track-work/about-milestones
- GitHub Docs — About Projects  
  https://docs.github.com/en/issues/planning-and-tracking-with-projects/learning-about-projects/about-projects
- GitHub Docs — Project automations  
  https://docs.github.com/en/issues/planning-and-tracking-with-projects/automating-your-project/using-the-built-in-automations
- GitHub Docs — Linking pull requests to issues  
  https://docs.github.com/en/issues/tracking-your-work-with-issues/using-issues/linking-a-pull-request-to-an-issue
- GitHub Docs — About releases  
  https://docs.github.com/en/repositories/releasing-projects-on-github/about-releases
- GitHub Docs — GitHub Packages  
  https://docs.github.com/en/packages/learn-github-packages/introduction-to-github-packages
- Git documentation — `git switch`, `git fetch`, and `git pull`  
  https://git-scm.com/docs/git-switch  
  https://git-scm.com/docs/git-fetch  
  https://git-scm.com/docs/git-pull
- Semantic Versioning 2.0.0  
  https://semver.org/
