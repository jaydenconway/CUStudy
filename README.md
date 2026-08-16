# CUStudy v0.1.0

## Description
A command-line tool for checking study room availability at Carleton University's MacOdrum Library. It pulls live availability from the library's booking system and draws the whole day as a timeline, so you can see which rooms are open when without scrolling through the booking grid one room at a time.

## Features
- Live room availability pulled straight from the library's booking system.
- Timeline view of the full day, one row per room.
- Real room numbers and capacities, not internal IDs.
- Rooms sorted by number, grouped in blocks of five for readability.
- (Planned) Filter by minimum block length, capacity, and floor type.
- (Planned) Watch mode that alerts when a room opens up.
- (Planned) Direct booking links for each room.

Floors 3 and 5 are silent floors. Floors 2 and 4 allow conversation.

## Requirements
- `Java 17` or higher.
- `Maven` — [install instructions](https://maven.apache.org/install.html). Gson downloads automatically.

## Installation
1. Clone the repository:
```bash
git clone https://github.com/jaydenconway/CUStudy.git
```
2. Navigate to the project directory:
```bash
cd CUStudy
```
3. Build the jar:
```bash
mvn package
```

## Usage
Run from the project root so that `rooms.txt` can be found:
```bash
java -jar target/custudy.jar
```

## Notes
CUStudy only reads availability. It does not book rooms — book through the library at https://carletonu.libcal.com

Room numbers and capacities in `rooms.txt` were collected by hand and may be out of date.

Not affiliated with Carleton University or MacOdrum Library.
