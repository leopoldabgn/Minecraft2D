#!/bin/bash

# remplacer les .webp en .png

for file in *.webp; do
    filename=$(basename "$file");
    filename="${filename%.*}";
    convert "$file" "$filename.png";
done

# Mettre tout les noms de fichier en minuscule

for file in *; do
    if [[ -f "$file" ]]; then
        lowercase_file=$(echo "$file" | tr '[:upper:]' '[:lower:]')
        if [[ "$file" != "$lowercase_file" ]]; then
            mv "$file" "$lowercase_file"
        fi
    fi
done
