#!make

.PHONY: install
install:
	npm install

.PHONY: dev
dev:
	npm run dev

.PHONY: check
check:
	npm run check

.PHONY: package
package:
	npm run package

.PHONY: build
build:
	npm run build

.PHONY: lint
lint:
	npm run lint

.PHONY: test
test:
	npm run test

.PHONY: fmt
fmt:
	npm run format

.PHONY: publish
publish:
	npm run publish

.PHONY: clean
clean:
	rm -rf dist
