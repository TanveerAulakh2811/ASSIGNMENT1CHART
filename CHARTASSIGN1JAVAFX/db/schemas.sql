DROP DATABASE anime_db;

-- Create the database if it doesn't exist
CREATE DATABASE IF NOT EXISTS anime_db;

-- Use the database
USE anime_db;

-- Table for storing genres and descriptions
CREATE TABLE genres (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        genre_name VARCHAR(255) NOT NULL,
                        description TEXT
);

-- Table for storing average ratings of genres
CREATE TABLE genre_ratings (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               genre_id INT,
                               average_rating DECIMAL(3, 1), -- Assuming average ratings are stored with one decimal place
                               FOREIGN KEY (genre_id) REFERENCES genres(id)
);

-- Insert sample data into genres table
INSERT INTO genres (genre_name, description)
VALUES
    ('Action', 'High-paced sequences, battles, and heroic quests'),
    ('Adventure', 'Characters embark on journeys to discover new places and face challenges'),
    ('Comedy', 'Focuses on humour and entertaining situations'),
    ('Drama', 'Intense character development and emotional narratives'),
    ('Fantasy', 'Magical elements, fantastical worlds, often involves quests and magic'),
    ('Science Fiction', 'Futuristic technology, space exploration, time travel'),
    ('Romance', 'Love stories and relationships'),
    ('Slice of Life', 'Everyday experiences, often character-focused'),
    ('Horror', 'Designed to scare and thrill, includes supernatural elements'),
    ('Mystery', 'Involves solving puzzles or crimes'),
    ('Thriller', 'High tension and suspense, often includes psychological elements'),
    ('Supernatural', 'Involves ghosts, spirits, and otherworldly phenomena'),
    ('Psychological', 'Explores mental states, often dark and introspective'),
    ('Sports', 'Focuses on athletic competitions and personal growth'),
    ('Music', 'Centres around musicians and the music industry'),
    ('Mecha', 'Giant robots and mechanical suits, often in sci-fi settings'),
    ('Historical', 'Set in a specific historical period, often with attention to detail'),
    ('Military', 'Focuses on armed forces, warfare, and strategy'),
    ('Martial Arts', 'Hand-to-hand combat, traditional martial arts techniques');

-- Insert sample data into genre_ratings table
-- Replace the average ratings with actual ratings from your source
INSERT INTO genre_ratings (genre_id, average_rating)
VALUES
    (1, 8.2),
    (2, 2.0),
    (3, 5.8),
    (4, 4.3),
    (5, 8.1),
    (6, 1.2),
    (7, 6.9),
    (8, 8.0),
    (9, 6.6),
    (10, 8.1),
    (11, 8.0),
    (12, 3.8),
    (13, 2.0),
    (14, 7.9),
    (15, 5.7),
    (16, 7.8),
    (17, 9.0),
    (18, 7.7),
    (19, 7.8);

-- Add more entries for genres and ratings as per your updated table
