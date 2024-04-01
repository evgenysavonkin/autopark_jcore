<%@ page import="java.util.Set" %>
<%@ page import="org.evgenysav.infrastructure.dto.classes_dto.VehicleDto" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="java.util.concurrent.atomic.AtomicReference" %>
<%@ page import="java.util.function.Predicate" %>
<%@ page import="java.util.Optional" %>
<%@ page contentType="text/html;charset=utf-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Автомобили</title>
    <link href="${pageContext.request.contextPath}/resources/css/style.css" rel="stylesheet">
</head>
<body>
<div class="center flex full-vh">
    <div class="vertical-center">
        <%
            List<VehicleDto> dtoList = (List<VehicleDto>) request.getAttribute("cars");
            Set<String> uniqTypes =
                    dtoList.stream().map(VehicleDto::getTypeName).collect(Collectors.toSet());
            Set<String> uniqModels =
                    dtoList.stream().map(VehicleDto::getModelName).collect(Collectors.toSet());
            Set<String> uniqueColors =
                    dtoList.stream().map(VehicleDto::getColor).collect(Collectors.toSet());
            Set<String> uniqEngineNames =
                    dtoList.stream().map(VehicleDto::getEngineName).collect(Collectors.toSet());
            AtomicReference<Predicate<VehicleDto>> filter = new AtomicReference<>(vehicleDto -> true);
            Optional.ofNullable(request.getParameter("type")).filter(s ->
                    !s.isEmpty()).ifPresent(s -> {
                filter.set(filter.get().and(vehicleDto -> vehicleDto.getTypeName().equals(s)));
            });

            Optional.ofNullable(request.getParameter("engine")).filter(s -> !s.isEmpty()).ifPresent(s -> {
                filter.set(filter.get().and(vehicleDto -> vehicleDto.getEngineName().equals(s)));
            });

            dtoList = dtoList.stream()
                    .filter(filter.get()).toList();
        %>

        <a class="ml-20" href="/">На главную</a>
        <a class="ml-20" href="/viewCars">Очистить фильтры</a>
        <br/>
        <br/>
        <hr/>
        <br/>
        <table>
            <caption>Автомобили</caption>
            <tr>
                <th>Тип</th>
                <th>Модель</th>
                <th>Регистрационный номер</th>
                <th>Масса</th>
                <th>Дата выпуска</th>
                <th>Цвет</th>
                <th>Тип двигателя</th>
                <th>Пробег</th>
            </tr>

            <%if (dtoList.size() == 0) {%>
            <tr>
                <td colspan="10">
                    Авто с заданными параметрами не найдены
                </td>
            </tr>
            <%}%>
            <%
                for (VehicleDto dto : dtoList) {
            %>
            <tr>
                <td><%=dto.getTypeName()%>
                </td>
                <td><%=dto.getModelName()%>
                </td>

                <td><%=dto.getRegistrationNumber()%>
                </td>

                <td><%=dto.getWeight()%>
                </td>

                <td><%=dto.getManufactureYear()%>
                </td>

                <td><%=dto.getColor()%>
                </td>
                <td><%=dto.getEngineName()%>
                </td>

                <td><%=dto.getMileage()%>
                </td>
            </tr>
            <%}%>
        </table>
        <br/>
        <div>
            <hr/>
            <br/>
            <form method="get" action="/viewCars" class="flex">
                <div>
                    <p>Тип</p>
                    <select name="type">
                        <option value=""
                                <%=request.getParameter("type") == null ? "selected" : ""%>>Не выбрано
                        </option>
                        <%
                            for (String s : uniqTypes) {
                        %>
                        <option value="<%=s%>"
                                <%=(request.getParameter("type") != null && s.equals(request.getParameter("type")) ? "selected" : "")%>><%=s%>
                        </option>
                        <%}%>
                    </select>
                </div>
                <div class="ml-20">
                    <p>Модель</p>
                    <select name="model">
                        <option value=""
                                <%=request.getParameter("model") == null ? "selected" : ""%>>Не выбрано
                        </option>
                        <%
                            for (String s : uniqModels) {
                        %>
                        <option value="
                        <%=s%>"
                                <%=(request.getParameter("model") != null && s.equals(request.getParameter("model")) ? "selected" : ""
                                )%>><%=s%>
                        </option>
                        <%}%>
                    </select>
                </div>

                <div class="ml-20">
                    <p>Двигатель</p>
                    <select name="engine">
                        <option value=""
                                <%=request.getParameter("engine") == null ? "selected" : ""%>>Не выбрано
                        </option>
                        <%for (String s : uniqEngineNames) {%>
                        <option value="<%=s%>"
                                <%=(request.getParameter("engine") != null &&
                                        s.equals(request.getParameter("engine")) ? "selected" : "")%>><%=s%>
                        </option>
                        <%}%>
                    </select>
                </div>
                <div class="ml-20">
                    <p>Цвет</p>
                    <select name="color">
                        <option value=""
                                <%=request.getParameter("color") == null ? "selected" : ""%>>Не выбрано
                        </option>
                        <%for (String s : uniqueColors) {%>
                        <option value="<%=s%>"
                                <%=(request.getParameter("color") != null &&
                                        s.equals(request.getParameter("color")) ? "selected" : "")%>><%=s%>
                        </option>
                        <%}%>
                    </select>
                </div>
                <button class="ml-20" type="submit">Выбрать</button>
            </form>
            <br/>
            <hr/>
        </div>
    </div>
</div>
</body>
</html>