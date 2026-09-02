.DEFAULT_GOAL := help

COMPOSE := docker compose

.PHONY: help up down logs ps test clean

help:
	@echo "up     Start the full Aven platform"
	@echo "down   Stop the platform and remove volumes"
	@echo "logs   Follow platform logs"
	@echo "ps     Show platform status"
	@echo "test   Run all service tests"
	@echo "clean  Remove local build output"

up:
	$(COMPOSE) up --build --detach

down:
	$(COMPOSE) down --volumes --remove-orphans

logs:
	$(COMPOSE) logs --follow

ps:
	$(COMPOSE) ps

test:
	@for service in ledger account settlement reconciliation gateway; do \
		(cd services/$$service && MAVEN_USER_HOME=$$(pwd)/../../.m2 ./mvnw test); \
	done

clean:
	@find services -type d -name target -prune -exec rm -rf {} +
