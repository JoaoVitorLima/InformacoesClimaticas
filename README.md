# InformacoesClimaticas
Este é um aplicativo em Java que consome a API do WeatherAPI para fornecer informações meteorológicas em tempo real para qualquer cidade do mundo.

O código consome a API da WeatherAPI(https://www.weatherapi.com/) para buscar os dados climáticos de uma cidade definida pelo usuário e exibí-la em tempo real.
Para isso, o código utiliza de tecnologias como o Java HTTP Client para fazer requisições HTTP à API meteorológica, o JSON.org para parsing e manipulação de dados JSON retornados pela API e o Java NIO para leitura dos arquivos e sua manipulação:

<img width="1140" height="773" alt="Image" src="https://github.com/user-attachments/assets/cf33773a-6bbe-46c7-a090-40bcdfdfd3cf" />

Para a exibição dos dados, ele busca os dados JSON retornados pela API e separam as informações necessárias para exibição para o usuário pelo console:

<img width="1000" height="760" alt="Image" src="https://github.com/user-attachments/assets/816678e3-eb1b-4589-ad36-f8a3826471d0" />

O usuário basta  apenas digitar o nome da cidade pelo console, que serão exibidas as suas informações climáticas:

<img width="430" height="254" alt="Image" src="https://github.com/user-attachments/assets/ac7671c4-2c05-4c24-9bf1-3a1e874f4623" />
