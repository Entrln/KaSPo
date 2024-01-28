run-kafka:
	docker network create app-tier --driver bridge

	# server: start broker (replace ^ with / for linux syntax.
	docker run -d --name kafka-server --hostname kafka-server --network app-tier ^
	-e KAFKA_MESSAGE_MAX_BYTES=1024 ^
	-e KAFKA_CFG_NODE_ID=0 ^
	-e KAFKA_CFG_PROCESS_ROLES=controller,broker ^
	-e KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093,EXTERNAL://:9094 ^
	-e KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,EXTERNAL:PLAINTEXT,PLAINTEXT:PLAINTEXT ^
	-e KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=0@kafka-server:9093 ^
	-e KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER ^
	-e KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://kafka-server:9092,EXTERNAL://localhost:9094 ^
	bitnami/kafka:latest

	# server: kafka server without EXTERNAL listeners
	docker run -d --name kafka-server --hostname kafka-server --network app-tier ^
	-e KAFKA_MESSAGE_MAX_BYTES=1024 ^
	-e KAFKA_CFG_NODE_ID=0 ^
	-e KAFKA_CFG_PROCESS_ROLES=controller,broker ^
	-e KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093 ^
	-e KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,EXTERNAL:PLAINTEXT,PLAINTEXT:PLAINTEXT ^
	-e KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=0@kafka-server:9093 ^
	-e KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER ^
	bitnami/kafka:latest

	# client: create topics
	docker run -it --rm  --network app-tier bitnami/kafka:latest kafka-topics.sh --create --topic testopic --bootstrap-server kafka-server:9092

	# client: List topics
	docker run -it --rm --network app-tier bitnami/kafka:latest kafka-topics.sh --list  --bootstrap-server kafka-server:9092


	#######################################################
	# Database
	#######################################################
	docker exec -it postgres psql -U postgres -c "CREATE DATABASE kaspodb;"

	docker run --name postgres -e POSTGRES_DB=kaspodb -p 5432:5432 -d postgres:latest
