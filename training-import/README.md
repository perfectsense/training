# Training Docker Setup

This directory contains a script for setting up content in the training
Docker.

Run the script in `/_debug/code` on a fresh Docker instance running
the training `web` war. The [run.sh](run.sh) script automates this process.

Exported Inspire Confidence content will be loaded from `export-training.json`
located in the same directory as the `docker-compose.yml` file. See
[Inspire Confidence Export](../ic-export) for obtaining content to import.
