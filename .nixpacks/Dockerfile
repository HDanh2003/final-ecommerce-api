FROM ghcr.io/railwayapp/nixpacks:ubuntu-1745885067

ENTRYPOINT ["/bin/bash", "-l", "-c"]
WORKDIR /app/


COPY .nixpacks/nixpkgs-59dc10b5a6f2a592af36375c68fda41246794b86.nix .nixpacks/nixpkgs-59dc10b5a6f2a592af36375c68fda41246794b86.nix
RUN nix-env -if .nixpacks/nixpkgs-59dc10b5a6f2a592af36375c68fda41246794b86.nix && nix-collect-garbage -d


ARG NIXPACKS_METADATA
ENV NIXPACKS_METADATA=$NIXPACKS_METADATA

# setup phase
# noop

# build phase
COPY . /app/.
RUN --mount=type=cache,id=87qqQDs10E-m2/repository,target=/app/.m2/repository chmod +x ./mvnw && ./mvnw -DoutputFile=target/mvn-dependency-list.log -B -DskipTests clean dependency:list install




# start
COPY . /app

CMD ["java -Dserver.port=$PORT $JAVA_OPTS -jar target/*jar"]

