#!make

.PHONY: install
install:
	yarn
	yarn install

.PHONY: dev
dev:
	yarn run dev

.PHONY: check
check:
	yarn run check

.PHONY: package
package:
	yarn run package

.PHONY: build
build:
	yarn run build

.PHONY: lint
lint:
	yarn run lint

.PHONY: test
test:
	yarn run test

.PHONY: fmt
fmt:
	yarn run format

.PHONY: clean
clean:
	rm -rf dist
