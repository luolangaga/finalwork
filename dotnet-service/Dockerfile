FROM mcr.microsoft.com/dotnet/sdk:9.0 AS build
WORKDIR /src
COPY dotnet-service/*.csproj ./
RUN dotnet restore
COPY dotnet-service/ ./
RUN dotnet publish -c Release -o /app

FROM mcr.microsoft.com/dotnet/aspnet:9.0
WORKDIR /app
COPY --from=build /app .
EXPOSE 8081
ENTRYPOINT ["dotnet", "DotnetService.dll"]