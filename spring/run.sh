#!/bin/bash

DEV_MODE=0

for arg in "$@"; do
    case $arg in
        -d|--dev)
            DEV_MODE=1
            shift
            ;;
        -*|--*)
            echo "Unknown option $arg"
            exit 1
            ;;
        *)
            ;;
    esac
done

mvn -q clean
if [[ $DEV_MODE -eq 1 ]]; then
    mvn test
fi
mvn -q spring-boot:run