# CUStudy v0.1.0

## Description
A command-line tool for checking study room availability at Carleton University's MacOdrum Library. It pulls live availability from the library's booking system and prints which rooms are free and when, so you can see the whole day at once instead of scanning the booking grid room by room. Room names and capacities are stored locally in a plain text file.

## Features
- Fetch live room availability for the current day.
- Group time slots by room and display them with real room numbers.
- Room capacities included so you can match a room to your group size.
- Silent floors (3 and 5) and conversational floors (2 and 4) distinguished by room number.
- (Planned) Collapse time slots into readable ranges, e.g. `09:00-13:00, 15:00-18:30`.
- (Planned) Filter by minimum block length, capacity, and floor type.
- (Planned) Visual timeline bars showing a room's full day at a glance.

## Requirements
- `Java 17` or higher.
- `Gson` for JSON parsing.

## Installation
1. Clone the repository:
```bash
git clone https://github.com/jaydenconway/custudy.git
```
2. Navigate to the project directory:
```bash
cd custudy
```
3. Add Gson to your classpath. In IntelliJ: File > Project Structure > Libraries > + > From Maven, then enter:
```
com.google.code.gson:gson:2.11.0
```

## Usage
Run the checker from the project root, so that `rooms.txt` can be found:
```bash
java CUStudy
```

## Notes
CUStudy only reads availability. It does not book rooms — book through the library at https://carletonu.libcal.com

Room data in `rooms.txt` was collected by hand and may be out of date.

Not affiliated with Carleton University or MacOdrum Library.
